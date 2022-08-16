package me.thiagorigonatti.avaliacaofullstack.service;

import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import me.thiagorigonatti.avaliacaofullstack.domain.User;
import me.thiagorigonatti.avaliacaofullstack.dto.TransferDTOPostBody;
import me.thiagorigonatti.avaliacaofullstack.exception.BadRequestException;
import me.thiagorigonatti.avaliacaofullstack.repository.AccountRepository;
import me.thiagorigonatti.avaliacaofullstack.repository.AccountRepositoryTest;
import me.thiagorigonatti.avaliacaofullstack.repository.TransferRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DataJpaTest
class BankServiceTest {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final AccountService accountService = new AccountService(accountRepository);

    private final BankService bankService = new BankService(transferRepository, accountService);


    private Account depositor() {
        User user = AccountRepositoryTest.createUser();
        user.setSsn("29864");
        user.setEmail("test2@testando.test");
        user.setName("Tester2");

        Account account = new Account();
        account.setUser(user);
        account.setNumber(String.valueOf(user.hashCode()).substring(0, 6));
        account.setBalance(BigDecimal.valueOf(100));
        account.setId(1L);
        return account;
    }


    private Account receiver() {
        User user = AccountRepositoryTest.createUser();
        user.setSsn("55763");
        user.setEmail("receiver2@testando.test");
        user.setName("Receiver2");

        Account account = new Account();
        account.setUser(user);
        account.setNumber(String.valueOf(user.hashCode()).substring(0, 6));
        account.setBalance(BigDecimal.valueOf(100));
        account.setId(2L);
        return account;
    }


    private TransferDTOPostBody create() {

        TransferDTOPostBody transferDTOPostBody = new TransferDTOPostBody();
        transferDTOPostBody.setDepositorAccountNumber(depositor().getNumber());
        transferDTOPostBody.setReceiverAccountNumber(receiver().getNumber());
        transferDTOPostBody.setAmount(BigDecimal.valueOf(1L));
        transferDTOPostBody.setType(TransferDTOPostBody.Type.A);
        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusHours(1));

        return transferDTOPostBody;

    }


    @Test
    @DisplayName("Matches the tax value for Type A if success")
    void calculates_TaxType_A_IfSuccessful() {
        TransferDTOPostBody transferDTOPostBody = create();
        transferDTOPostBody.setType(TransferDTOPostBody.Type.A);
        transferDTOPostBody.setTransferDate(LocalDateTime.now());

        boolean sameDay = transferDTOPostBody.getTransferDate().toLocalDate()
                .isEqual(LocalDateTime.now().toLocalDate());

        Assertions.assertThat(sameDay).isTrue();

        BigDecimal tax = new BankService(transferRepository,
                new AccountService(accountRepository)).calculateTaxTypeA(create());

        Assertions.assertThat(tax).isEqualByComparingTo(BigDecimal.valueOf(3.03));
    }

    @Test
    @DisplayName("Matches the tax value for Type B if success")
    void calculates_TaxType_B_IfSuccessful() {
        TransferDTOPostBody transferDTOPostBody = create();
        transferDTOPostBody.setType(TransferDTOPostBody.Type.B);
        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(9));
        BigDecimal tax = bankService.calculateTaxTypeB(transferDTOPostBody);
        Assertions.assertThat(tax).isEqualByComparingTo(BigDecimal.valueOf(12));
        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(11));
        Assertions.assertThatThrownBy(() ->
                bankService.calculateTaxTypeB(transferDTOPostBody)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Matches the tax value for Type C if success")
    void calculates_TaxType_C_IfSuccessful() {
        TransferDTOPostBody transferDTOPostBody = create();
        transferDTOPostBody.setType(TransferDTOPostBody.Type.C);
        BigDecimal tax;

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(41));
        tax = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(tax).isEqualByComparingTo(BigDecimal.valueOf(0.017));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(31));
        tax = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(tax).isEqualByComparingTo(BigDecimal.valueOf(0.047));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(21));
        tax = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(tax).isEqualByComparingTo(BigDecimal.valueOf(0.069));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(11));
        tax = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(tax).isEqualByComparingTo(BigDecimal.valueOf(0.082));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(9));
        Assertions.assertThatThrownBy(() ->
                bankService.calculateTaxTypeC(transferDTOPostBody)).isInstanceOf(BadRequestException.class);

    }

    void setType(TransferDTOPostBody transferDTOPostBody) {
        if (transferDTOPostBody.getAmount().compareTo(BigDecimal.valueOf(2000)) > 0) {
            transferDTOPostBody.setType(TransferDTOPostBody.Type.C);
        } else if (transferDTOPostBody.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
            transferDTOPostBody.setType(TransferDTOPostBody.Type.B);
        } else {
            transferDTOPostBody.setType(TransferDTOPostBody.Type.A);
        }
    }

    @Test
    @DisplayName("Matches the tax value for Type D if success")
    void calculates_TaxType_D_IfSuccessful() {
        TransferDTOPostBody transferDTOPostBody = create();
        transferDTOPostBody.setType(TransferDTOPostBody.Type.D);


        transferDTOPostBody.setAmount(BigDecimal.valueOf(100));
        boolean sameDay = transferDTOPostBody.getTransferDate().toLocalDate()
                .isEqual(LocalDateTime.now().toLocalDate());
        Assertions.assertThat(sameDay).isTrue();
        BigDecimal taxA = new BankService(transferRepository,
                new AccountService(accountRepository)).calculateTaxTypeA(create());
        Assertions.assertThat(taxA).isEqualByComparingTo(BigDecimal.valueOf(3.03));


        transferDTOPostBody.setAmount(BigDecimal.valueOf(1001));
        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(9));
        BigDecimal taxB = bankService.calculateTaxTypeB(transferDTOPostBody);
        Assertions.assertThat(taxB).isEqualByComparingTo(BigDecimal.valueOf(12));
        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(11));
        Assertions.assertThatThrownBy(() ->
                bankService.calculateTaxTypeB(transferDTOPostBody)).isInstanceOf(BadRequestException.class);


        transferDTOPostBody.setAmount(BigDecimal.valueOf(2001));
        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(41));
        BigDecimal taxC = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(taxC).isEqualByComparingTo(BigDecimal.valueOf(34.0170));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(31));
        taxC = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(taxC).isEqualByComparingTo(BigDecimal.valueOf(94.0470));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(21));
        taxC = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(taxC).isEqualByComparingTo(BigDecimal.valueOf(138.0690));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(11));
        taxC = bankService.calculateTaxTypeC(transferDTOPostBody);
        Assertions.assertThat(taxC).isEqualByComparingTo(BigDecimal.valueOf(164.0820));

        transferDTOPostBody.setTransferDate(LocalDateTime.now().plusDays(9));
        Assertions.assertThatThrownBy(() ->
                bankService.calculateTaxTypeC(transferDTOPostBody)).isInstanceOf(BadRequestException.class);

    }
}
