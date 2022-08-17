package entities;

public class Product {
    private int id;
    private String productName;
    private int productCost;

    public Product(int id, String productName, int productCost) {
        this.id = id;
        this.productName = productName;
        this.productCost = productCost;
    }
}
