package org.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class BankAccountServiceTest {
    @Test
    @Tag("survived")
    void withdrawFromBalanceUpdatesAccountFieldsCorrectlySurvived() {
        BankAccountRepository repository = new BankAccountRepository();
        repository.addAccount("John Doe", 1000);
        BankAccountService service = new BankAccountService(repository);
        long id = repository.getAllAccounts().getFirst().getId();
        service.withdrawFromBalance(id, 500);
        BankAccountModel account = service.getAccountById(id);
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(account.getId()).isEqualTo(id);
                softAssertions.assertThat(account.getOwnerName()).isEqualTo("John Doe");
                softAssertions.assertThat(account.getAmount()).isEqualTo(500);
        });
    }

    @Test
    @Tag("killed")
    void withdrawFromBalanceUpdatesAccountFieldsCorrectlyKilled() {
        BankAccountRepository repository = new BankAccountRepository();
        repository.addAccount("John Doe", 1000);
        BankAccountService service = new BankAccountService(repository);
        long id = repository.getAllAccounts().getFirst().getId();
        service.withdrawFromBalance(id, 1000);
        BankAccountModel account = service.getAccountById(id);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(account.getId()).isEqualTo(id);
            softAssertions.assertThat(account.getOwnerName()).isEqualTo("John Doe");
            softAssertions.assertThat(account.getAmount()).isEqualTo(0);
        });
    }

    @Test
    void getAllAccountsReturnsEverythingAdded() {
        BankAccountRepository repository = new BankAccountRepository();
        repository.addAccount("Mary Jane", 500);
        repository.addAccount("John Doe", 1000);
        repository.addAccount("Joe Mama", 6767);
        BankAccountService service = new BankAccountService(repository);
        List<BankAccountModel> allAccounts = service.getAllAccounts();
        assertThat(allAccounts).hasSize(3);
        assertThat(allAccounts).extracting(BankAccountModel::getOwnerName).containsExactly("Mary Jane", "John Doe", "Joe Mama");
        assertThat(allAccounts).allMatch(account -> account.getAmount() > 0);
    }
}
