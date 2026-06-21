package org.example;

import java.util.ArrayList;
import java.util.List;

public class BankAccountRepository {
    private final ArrayList<BankAccountModel> database;
    private int freeId;

    public BankAccountRepository() {
        this.database = new ArrayList<BankAccountModel>();
    }

    public void addAccount(String ownerName, double amount) {
        database.add(new BankAccountModel(freeId++, ownerName, amount));
    }

    public BankAccountModel getAccountById(long id) {
        BankAccountModel model = database.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
        if (model == null)
            throw new IllegalArgumentException("No bank account with id " + id);
        return model;
    }

    public void saveAccount(BankAccountModel account) {
        boolean found = false;
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getId() == account.getId()) {
                database.set(i, account);
                found = true;
                break;
            }
        }
        if (!found)
            throw new IllegalArgumentException("No bank account with id " + account.getId());
    }

    public List<BankAccountModel> getAllAccounts() {
        return new ArrayList<>(database);
    }
}
