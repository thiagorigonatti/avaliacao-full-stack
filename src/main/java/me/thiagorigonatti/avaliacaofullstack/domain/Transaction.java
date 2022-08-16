package me.thiagorigonatti.avaliacaofullstack.domain;

import me.thiagorigonatti.avaliacaofullstack.dto.TransferDTOPostBody;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account depositor;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account receiver;

    private BigDecimal amount;

    private BigDecimal tax;

    private TransferDTOPostBody.Type type;

    private LocalDateTime createdAt;

    private LocalDateTime transferDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getDepositor() {
        return depositor;
    }

    public void setDepositor(Account depositor) {
        this.depositor = depositor;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal balance) {
        this.amount = balance;
    }

    public enum Type {
        A, B, C, D
    }

    public TransferDTOPostBody.Type getType() {
        return type;
    }

    public void setType(TransferDTOPostBody.Type type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public static Transaction scheduledTransfer(Account depositor,
                                                Account receiver,
                                                BigDecimal amount,
                                                BigDecimal tax,
                                                TransferDTOPostBody.Type type,
                                                LocalDateTime createdAt,
                                                LocalDateTime transferDate) {

        Transaction transaction = new Transaction();

        transaction.setDepositor(depositor);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setTax(tax);
        transaction.setType(type);
        transaction.setCreatedAt(createdAt);
        transaction.setTransferDate(transferDate);

        return transaction;
    }
}
