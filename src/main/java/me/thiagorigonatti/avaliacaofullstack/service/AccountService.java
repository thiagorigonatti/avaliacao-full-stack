package me.thiagorigonatti.avaliacaofullstack.service;


import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import me.thiagorigonatti.avaliacaofullstack.domain.Transaction;
import me.thiagorigonatti.avaliacaofullstack.dto.AccountDTOPostBody;
import me.thiagorigonatti.avaliacaofullstack.dto.TransferDTOPostBody;
import me.thiagorigonatti.avaliacaofullstack.exception.BadRequestException;
import me.thiagorigonatti.avaliacaofullstack.mapper.AccountMapper;
import me.thiagorigonatti.avaliacaofullstack.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class AccountService {

    private final AccountRepository accountRepository;


    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new BadRequestException("Account not found"));
    }

    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number).orElseThrow(() -> new BadRequestException("Account not found"));
    }

    public void makeTransfer(Account receiver, BigDecimal amount) {
        add(receiver, amount);
    }

    @Transactional
    public void add(Account receiver, BigDecimal amount) {
        receiver.setBalance(receiver.getBalance().add(amount));
        accountRepository.save(receiver);
    }

    @Transactional
    public void withdraw(Account depositor, BigDecimal total) {
        depositor.setBalance(depositor.getBalance().subtract(total));
        accountRepository.save(depositor);
    }

    @Transactional
    public Account save(AccountDTOPostBody accountDTOPostBody) {

        List<Account> accounts = findAll();

        if (accounts.stream().map(account -> account.getUser().getEmail()).toList()
                .contains(accountDTOPostBody.getEmail()))
            throw new BadRequestException("Email already taken");

        if (accounts.stream().map(account -> account.getUser().getSsn()).toList()
                .contains(accountDTOPostBody.getSsn()))
            throw new BadRequestException("ssn already inserted");

        return accountRepository.save(AccountMapper.INSTANCE.toAccount(accountDTOPostBody));
    }

    public Transaction transfer(Account depositor,
                                Account receiver,
                                BigDecimal amount,
                                BigDecimal tax,
                                TransferDTOPostBody.Type type,
                                LocalDateTime createdAt,
                                LocalDateTime transferDate) {

        BigDecimal total = amount.add(tax);

        if (depositor.getBalance().compareTo(total) < 0) throw new BadRequestException("Insufficient funds");

        withdraw(depositor, total);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                makeTransfer(receiver, amount);
                this.cancel();
            }
        }, Date.from(transferDate.toInstant(ZoneId.systemDefault().getRules().getOffset(transferDate))));

        return Transaction.scheduledTransfer(depositor, receiver, amount, tax, type, createdAt, transferDate);
    }
}
