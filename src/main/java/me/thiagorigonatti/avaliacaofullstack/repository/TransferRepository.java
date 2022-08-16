package me.thiagorigonatti.avaliacaofullstack.repository;

import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import me.thiagorigonatti.avaliacaofullstack.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByDepositor(Account depositor);
}
