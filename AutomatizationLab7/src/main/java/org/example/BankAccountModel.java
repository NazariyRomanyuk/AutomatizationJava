package org.example;

public class BankAccountModel {
    private long id;
    private String ownerName;
    private double amount;

    public BankAccountModel(long id, String ownerName, double amount) {
        this.id = id;
        this.ownerName = ownerName;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
