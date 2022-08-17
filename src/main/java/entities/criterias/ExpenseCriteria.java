package entities.criterias;

public class ExpenseCriteria {
    private int minExpenses;
    private int maxExpenses;

    public ExpenseCriteria(int minExpenses, int maxExpenses) {
        this.minExpenses = minExpenses;
        this.maxExpenses = maxExpenses;
    }

    public int getMinExpenses() {
        return minExpenses;
    }

    public void setMinExpenses(int minExpenses) {
        this.minExpenses = minExpenses;
    }

    public int getMaxExpenses() {
        return maxExpenses;
    }

    public void setMaxExpenses(int maxExpenses) {
        this.maxExpenses = maxExpenses;
    }
}
