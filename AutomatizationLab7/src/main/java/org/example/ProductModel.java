package org.example;

public class ProductModel {
    private long id;
    private String name;
    private double price;
    private int amountInStock;

    public ProductModel(long id, String name, double price, int amountInStock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amountInStock = amountInStock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }
}
