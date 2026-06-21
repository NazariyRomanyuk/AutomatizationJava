package org.example;

import java.util.List;

public class ProductService {
    private final Warehouse warehouse;

    public ProductService(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void addProduct(String name, double price, int amountInStock) {
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative!");
        if (amountInStock < 0)
            throw new IllegalArgumentException("Amount in stock cannot be negative!");
        warehouse.addProduct(name, price, amountInStock);
    }

    public ProductModel getProductById(long id) {
        return warehouse.getProductById(id);
    }

    public void takeProductFromWarehouse(long id, int amount) {
        ProductModel product = warehouse.getProductById(id);
        int newAmount = product.getAmountInStock() - amount;
        if (newAmount < 0)
            throw new IllegalArgumentException("Not enough " + product.getName() + " left in stock!");
        product.setAmountInStock(newAmount);
        warehouse.saveProduct(product);
    }

    public void addProductToWarehouse(long id, int amount) {
        ProductModel product = warehouse.getProductById(id);
        int newAmount = product.getAmountInStock() + amount;
        product.setAmountInStock(newAmount);
        warehouse.saveProduct(product);
    }

    public List<ProductModel> getAllProducts() {
        return warehouse.getAllProducts();
    }

}
