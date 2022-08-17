package entities.criterias;

public class BadCustomerCriteria {
    private int badCustomers;

    public BadCustomerCriteria(int badCustomers) {
        this.badCustomers = badCustomers;
    }

    public int getBadCustomers() {
        return badCustomers;
    }

    public void setBadCustomers(int badCustomers) {
        this.badCustomers = badCustomers;
    }
}
