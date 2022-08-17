package entities;

public class Purchase {
    private String customerName;
    private String purchaseName;
    private String purchaseDate;

    public Purchase(String customerName, String purchaseName, String purchaseDate) {
        this.customerName = customerName;
        this.purchaseName = purchaseName;
        this.purchaseDate = purchaseDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
