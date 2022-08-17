package serializers.stat;

import java.util.ArrayList;
import java.util.List;

public class StatOut {
    private final String type = "stat";
    private int totalDays;
    private List<PersonOut> customers;
    private int totalExpenses;
    private float avgExpenses;

    public StatOut() {
        this.customers = new ArrayList<>();
    }

    public void addPerson(PersonOut person) {
        customers.add(person);
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public void setAvgExpenses(float avgExpenses) {
        this.avgExpenses = avgExpenses;
    }
}
