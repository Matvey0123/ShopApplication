package serializers.stat;

import java.util.ArrayList;
import java.util.List;

public class PersonOut {
    private String name;
    private List<PurchaseOut> purchases;
    private int totalExpenses;

    public PersonOut(String name) {
        this.name = name;
        this.purchases = new ArrayList<>();
    }

    public void addPurchase(PurchaseOut purchase) {
        purchases.add(purchase);
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}
