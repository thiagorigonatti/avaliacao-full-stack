package me.thiagorigonatti.avaliacaofullstack.repository;

import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumber(String number);

}
