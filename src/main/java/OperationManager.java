import com.google.gson.Gson;
import entities.Customer;
import entities.Purchase;
import entities.criterias.*;
import parsers.CriteriaParser;
import parsers.DateParser;
import serializers.search.SearchOutput;
import serializers.search.SearchResult;
import serializers.stat.PersonOut;
import serializers.stat.PurchaseOut;
import serializers.stat.StatOut;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationManager {

    public static void executeSearch(String pathFrom, String pathTo) throws SQLException, IOException {
        DatabaseManager dbManager = new DatabaseManager();
        Criterias criterias = CriteriaParser.parse(pathFrom);
        List<Customer> customers = null;
        SearchOutput searchOutput = new SearchOutput();
        Object criteriaOutput = null;
        for (Object criteriaObj: criterias.getCriterias()) {
            String criteriaStr = criteriaObj.toString();
            if (criteriaStr.contains("lastName")) {
                String[] parts = criteriaStr.split("=");
                String lastName = parts[1].split("}")[0];
                LastNameCriteria criteria = new LastNameCriteria(lastName);
                customers = dbManager.getCustomersByLastName(criteria);
                criteriaOutput = criteria;
            }
            if (criteriaStr.contains("productName")) {
                String[] parts = criteriaStr.split(", ");
                String productName = parts[0].split("=")[1];
                int minTimes = (int)Float.parseFloat(parts[1].split("=")[1].split("}")[0]);
                ProductNameCriteria criteria = new ProductNameCriteria(productName, minTimes);
                customers = dbManager.getCustomersByProduct(criteria);
                criteriaOutput = criteria;
            }
            if (criteriaStr.contains("minExpenses")) {
                String[] parts = criteriaStr.split(", ");
                int minExpenses = (int)Float.parseFloat(parts[0].split("=")[1]);
                int maxExpenses = (int)Float.parseFloat(parts[1].split("=")[1].split("}")[0]);
                ExpenseCriteria criteria = new ExpenseCriteria(minExpenses, maxExpenses);
                customers = dbManager.getCustomersByExpense(criteria);
                criteriaOutput = criteria;
            }
            if (criteriaStr.contains("badCustomers")) {
                String[] parts = criteriaStr.split("=");
                int badCustomers = (int)Float.parseFloat(parts[1].split("}")[0]);
                BadCustomerCriteria criteria = new BadCustomerCriteria(badCustomers);
                customers = dbManager.getBadCustomers(criteria);
                criteriaOutput = criteria;
            }
            SearchResult searchResult = new SearchResult(criteriaOutput, customers);
            searchOutput.addSearchResult(searchResult);
        }
        dbManager.close();
        String jsonString = new Gson().toJson(searchOutput, SearchOutput.class);
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathTo));
        writer.write(jsonString);

        writer.close();
    }

    public static void executeStat(String pathFrom, String pathTo) throws SQLException, IOException {
        DatabaseManager dbManager = new DatabaseManager();
        DateForStat date = DateParser.parse(pathFrom);
        StatOut statOut = new StatOut();

        LocalDate startDate = LocalDate.parse(date.getStartDate());
        LocalDate endDate = LocalDate.parse(date.getEndDate());
        int totalDays = countWorkDays(endDate, startDate, 0);
        statOut.setTotalDays(totalDays);
        int totalExpense = 0;

        List<String> allCustomers = dbManager.getAllCustomers();
        for (String customerName: allCustomers) {
            PersonOut personOut = new PersonOut(customerName);
            List<Purchase> allPurchases = dbManager.getPurchases(customerName);
            Map<String, Integer> nameToExpense = new HashMap<>();
            int totalUserExpense = 0;
            for (Purchase purchase: allPurchases) {
                LocalDate purchaseDate = LocalDate.parse(purchase.getPurchaseDate());
                if (isDateBetween(startDate, endDate, purchaseDate) && !isWeekend(purchaseDate)) {
                    int purchaseCost = dbManager.getPurchaseCost(purchase.getPurchaseName());
                    if(nameToExpense.containsKey(purchase.getPurchaseName())) {
                        int cost = nameToExpense.remove(purchase.getPurchaseName());
                        nameToExpense.put(purchase.getPurchaseName(), cost + purchaseCost);
                    } else {
                        nameToExpense.put(purchase.getPurchaseName(), purchaseCost);
                    }
                    totalUserExpense += purchaseCost;
                }
            }
            List<String> purchases = nameToExpense.keySet().stream().toList();
            for (String purchaseName: purchases) {
                personOut.addPurchase(new PurchaseOut(purchaseName, nameToExpense.remove(purchaseName)));
            }

            personOut.setTotalExpenses(totalUserExpense);
            totalExpense += totalUserExpense;
            statOut.addPerson(personOut);
        }
        statOut.setTotalExpenses(totalExpense);
        statOut.setAvgExpenses(totalExpense / allCustomers.size());
        dbManager.close();

        String jsonString = new Gson().toJson(statOut, StatOut.class);
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathTo));
        writer.write(jsonString);

        writer.close();
    }

    public static int countWorkDays(LocalDate to, LocalDate x, int cnt) {
        if (x.isAfter(to)) {
            return cnt;
        }
        if (!isWeekend(x)) {
            cnt++;
        }
        return countWorkDays(to, x.plusDays(1), cnt);
    }

    private static boolean isDateBetween(LocalDate from, LocalDate to, LocalDate x) {
        return x.isAfter(from) && x.isBefore(to);
    }

    private static boolean isWeekend(final LocalDate ld) {
        DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }
}
