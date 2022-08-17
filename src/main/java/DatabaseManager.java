import entities.Customer;
import entities.Purchase;
import entities.criterias.BadCustomerCriteria;
import entities.criterias.ExpenseCriteria;
import entities.criterias.LastNameCriteria;
import entities.criterias.ProductNameCriteria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String jdbcURL = "jdbc:postgresql://localhost:5432/ShopDB";
    private static final String username = "postgres";
    private static final String password = "postgres";

    private Connection connection;

    public DatabaseManager() throws SQLException {
        connection = DriverManager.getConnection(jdbcURL, username, password);
    }

    public void close() throws SQLException {
        connection.close();
    }

    public List<Customer> getCustomersByLastName(LastNameCriteria criteria) {
        String selectSql = "SELECT * FROM customers WHERE last_name = ?";
        List<Customer> results = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, criteria.getLastName());
            ResultSet resultSet = statement.executeQuery();
            results = fillCustomerList(resultSet);
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return results;
    }

    public List<Customer> getCustomersByProduct(ProductNameCriteria criteria) {
        String selectSql = "SELECT customer_name, COUNT(purchase_name) from purchases" +
                " WHERE purchase_name = ?" +
                " GROUP BY customer_name" +
                " HAVING COUNT(purchase_name) >= ?";
        List<Customer> results = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, criteria.getProductName());
            statement.setInt(2, criteria.getMinTimes());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fullName = resultSet.getString("customer_name");
                String[] parts = fullName.split(" ");
                results.add(new Customer(parts[0], parts[1]));
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return results;
    }

    public List<Customer> getCustomersByExpense(ExpenseCriteria criteria) {
        String selectSql = "SELECT customer_name, SUM(product_cost) from purchases" +
                " JOIN products ON purchase_name = product_name" +
                " GROUP BY customer_name" +
                " HAVING SUM(product_cost) >= ? AND SUM(product_cost) <= ?";
        List<Customer> results = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, criteria.getMinExpenses());
            statement.setInt(2, criteria.getMaxExpenses());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fullName = resultSet.getString("customer_name");
                String[] parts = fullName.split(" ");
                results.add(new Customer(parts[0], parts[1]));
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return results;
    }

    public List<Customer> getBadCustomers(BadCustomerCriteria criteria) {
        String selectSql = "SELECT customer_name, COUNT(purchase_name) from purchases" +
                " JOIN products ON purchase_name = product_name" +
                " GROUP BY customer_name" +
                " ORDER BY COUNT(purchase_name) ASC" +
                " LIMIT ?";
        List<Customer> results = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, criteria.getBadCustomers());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fullName = resultSet.getString("customer_name");
                String[] parts = fullName.split(" ");
                results.add(new Customer(parts[0], parts[1]));
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return results;
    }

    public List<String> getAllCustomers() {
        String selectSql = "SELECT DISTINCT customer_name FROM purchases";
        List<String> results = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                String fullName = resultSet.getString("customer_name");
                results.add(fullName);
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return results;
    }

    public List<Purchase> getPurchases(String customerName) {
        String selectSql = "SELECT purchase_name, purchase_date from purchases" +
                " WHERE customer_name = ?";
        List<Purchase> results = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, customerName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("purchase_name");
                String date = resultSet.getString("purchase_date");
                results.add(new Purchase(customerName, name, date));
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return results;
    }

    public int getPurchaseCost(String purchaseName) {
        String selectSql = "SELECT product_cost from products" +
                " WHERE product_name = ?";
        int cost = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, purchaseName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cost = resultSet.getInt("product_cost");
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return cost;
    }

    private List<Customer> fillCustomerList(ResultSet resultSet) throws SQLException {
        List<Customer> results = new ArrayList<>();
        while (resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            results.add(new Customer(firstName, lastName));
        }
        return results;
    }
}
