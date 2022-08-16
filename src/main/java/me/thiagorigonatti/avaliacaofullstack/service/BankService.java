package me.thiagorigonatti.avaliacaofullstack.service;

import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import me.thiagorigonatti.avaliacaofullstack.domain.Transaction;
import me.thiagorigonatti.avaliacaofullstack.dto.TransferDTOPostBody;
import me.thiagorigonatti.avaliacaofullstack.exception.BadRequestException;
import me.thiagorigonatti.avaliacaofullstack.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankService {

    private final TransferRepository transferRepository;
    private final AccountService accountService;

    public BankService(TransferRepository transferRepository, AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    public List<Transaction> findAllByDepositor(Account account) {
        return transferRepository.findAllByDepositor(account);
    }

    public BigDecimal calculateTaxTypeA(TransferDTOPostBody transferDTOPostBody) {

        if (transferDTOPostBody.getTransferDate().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
            return BigDecimal.valueOf(3).add(transferDTOPostBody.getAmount().multiply(BigDecimal.valueOf(0.03)));
        else throw new BadRequestException("Opção incorreta");
    }

    public BigDecimal calculateTaxTypeB(TransferDTOPostBody transferDTOPostBody) {
        if (transferDTOPostBody.getTransferDate().isBefore(LocalDateTime.now().plusDays(10)))
            return BigDecimal.valueOf(12);
        else throw new BadRequestException("Opção incorreta");
    }

    public BigDecimal calculateTaxTypeC(TransferDTOPostBody transferDTOPostBody) {
        if (transferDTOPostBody.getTransferDate().isAfter(LocalDateTime.now().plusDays(40)))
            return transferDTOPostBody.getAmount().multiply(BigDecimal.valueOf(0.017));

        else if (transferDTOPostBody.getTransferDate().isAfter(LocalDateTime.now().plusDays(30)))
            return transferDTOPostBody.getAmount().multiply(BigDecimal.valueOf(0.047));

        else if (transferDTOPostBody.getTransferDate().isAfter(LocalDateTime.now().plusDays(20)))
            return transferDTOPostBody.getAmount().multiply(BigDecimal.valueOf(0.069));

        else if (transferDTOPostBody.getTransferDate().isAfter(LocalDateTime.now().plusDays(10)))
            return transferDTOPostBody.getAmount().multiply(BigDecimal.valueOf(0.082));

        else throw new BadRequestException("Opção incorreta");
    }

    public BigDecimal calculateTaxTypeD(TransferDTOPostBody transferDTOPostBody) {

        if (transferDTOPostBody.getAmount().compareTo(BigDecimal.valueOf(2000)) > 0) {
            return calculateTaxTypeC(transferDTOPostBody);

        } else if (transferDTOPostBody.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
            return calculateTaxTypeB(transferDTOPostBody);

        } else {
            return calculateTaxTypeA(transferDTOPostBody);
        }
    }

    @Transactional
    public Transaction transfer(TransferDTOPostBody transferDTOPostBody) {

        if (transferDTOPostBody.getTransferDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("The transferDate must to be after than now");

        BigDecimal tax = null;

        switch (transferDTOPostBody.getType()) {
            case A:
                tax = calculateTaxTypeA(transferDTOPostBody);
                break;
            case B:
                tax = calculateTaxTypeB(transferDTOPostBody);
                break;
            case C:
                tax = calculateTaxTypeC(transferDTOPostBody);
                break;
            case D:
                tax = calculateTaxTypeD(transferDTOPostBody);
        }


        Transaction transaction = accountService.transfer(
                accountService.findByNumber(transferDTOPostBody.getDepositorAccountNumber()),
                accountService.findByNumber(transferDTOPostBody.getReceiverAccountNumber()),
                transferDTOPostBody.getAmount(),
                tax,
                transferDTOPostBody.getType(),
                LocalDateTime.now(),
                transferDTOPostBody.getTransferDate());

        return transferRepository.save(transaction);
    }
}
