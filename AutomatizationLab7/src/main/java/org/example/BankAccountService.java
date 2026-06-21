package org.example;

import java.util.ArrayList;
import java.util.List;

public class BankAccountService {
    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    public void addAccount(String ownerName, double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative!");
        repository.addAccount(ownerName, amount);
    }

    public BankAccountModel getAccountById(long id) {
        return repository.getAccountById(id);
    }

    public void withdrawFromBalance(long id, double amount) {
        BankAccountModel bankAccount = repository.getAccountById(id);
        double newAmount = bankAccount.getAmount() - amount;
        if (newAmount < 0)
            throw new IllegalArgumentException("Not enough money left!");
        bankAccount.setAmount(newAmount);
        repository.saveAccount(bankAccount);
    }

    public List<BankAccountModel> getAllAccounts() {
        return repository.getAllAccounts();
    }
}
