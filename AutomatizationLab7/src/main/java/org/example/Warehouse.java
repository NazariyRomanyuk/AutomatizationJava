package org.example;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final ArrayList<ProductModel> database;
    private int freeId;

    public Warehouse() {
        this.database = new ArrayList<>();
    }

    public void addProduct(String name, double price, int amountInStock) {
        database.add(new ProductModel(freeId++, name, price, amountInStock));
    }

    public ProductModel getProductById(long id) {
        ProductModel model = database.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
        if (model == null)
            throw new IllegalArgumentException("No product with id " + id);
        return model;
    }

    public void saveProduct(ProductModel product) {
        boolean found = false;
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getId() == product.getId()) {
                database.set(i, product);
                found = true;
                break;
            }
        }
        if (!found)
            throw new IllegalArgumentException("No product with id " + product.getId());
    }

    public List<ProductModel> getAllProducts() {
        return new ArrayList<>(database);
    }
}
