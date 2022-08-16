package me.thiagorigonatti.avaliacaofullstack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferDTOPostBody {

    @NotNull
    private String depositorAccountNumber;

    @NotNull
    private String receiverAccountNumber;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransferDTOPostBody.Type type;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime transferDate;

    public String getDepositorAccountNumber() {
        return depositorAccountNumber;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        A, B, C, D;
    }

    public void setDepositorAccountNumber(String depositorAccountNumber) {
        this.depositorAccountNumber = depositorAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }
}
