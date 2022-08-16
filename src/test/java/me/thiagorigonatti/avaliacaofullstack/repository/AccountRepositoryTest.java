package me.thiagorigonatti.avaliacaofullstack.repository;

import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import me.thiagorigonatti.avaliacaofullstack.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Account repository")
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    public static User createUser() {
        User user = new User();
        user.setName("Tester");
        user.setEmail("test@testando.test");
        return user;
    }

    private Account createAccount() {
        Account account = new Account();
        account.setNumber("018275");
        account.setBalance(BigDecimal.valueOf(0L));
        account.setUser(createUser());
        return account;
    }

    @Test
    @DisplayName("Find an account by id when success")
    void findById_ReturnsAnAccount_WhenSuccessful() {
        Account accountToBeSaved = createAccount();
        Account accountSaved = accountRepository.save(accountToBeSaved);

        Long id = accountSaved.getId();

        Optional<Account> accountOptional = accountRepository.findById(id);
        Assertions.assertThat(accountOptional).isNotEmpty();
        Assertions.assertThat(accountRepository.findAll().contains(accountOptional.get())).isTrue();
    }


    @Test
    @DisplayName("Saves persists an account when success")
    void save_PersistsAccount_WhenSuccessful() {
        Account accountToBeSaved = createAccount();
        Account accountSaved = accountRepository.save(accountToBeSaved);
        Assertions.assertThat(accountSaved).isNotNull();
        Assertions.assertThat(accountSaved.getId()).isNotNull();
        Assertions.assertThat(accountSaved.getNumber()).isEqualTo(accountToBeSaved.getNumber());
    }


    @Test
    @DisplayName("Saves updates an account when success")
    void save_UpdatesAccount_WhenSuccessful() {
        Account accountToBeSaved = createAccount();
        Account accountSaved = accountRepository.save(accountToBeSaved);

        accountSaved.setNumber("499896");
        Account accountUpdated = accountRepository.save(accountSaved);

        Assertions.assertThat(accountUpdated).isNotNull();
        Assertions.assertThat(accountUpdated.getId()).isNotNull();
        Assertions.assertThat(accountUpdated.getNumber()).isEqualTo(accountSaved.getNumber());
    }

    @Test
    @DisplayName("Delete removes an account when success")
    void delete_RemovesAccount_WhenSuccessful() {
        Account accountToBeSaved = createAccount();
        Account accountSaved = accountRepository.save(accountToBeSaved);
        accountRepository.delete(accountSaved);
        Optional<Account> accountOptional = accountRepository.findByNumber(accountSaved.getNumber());
        Assertions.assertThat(accountOptional).isEmpty();
    }

}