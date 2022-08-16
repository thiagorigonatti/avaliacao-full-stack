package me.thiagorigonatti.avaliacaofullstack.controller;

import me.thiagorigonatti.avaliacaofullstack.domain.Transaction;
import me.thiagorigonatti.avaliacaofullstack.dto.TransferDTOPostBody;
import me.thiagorigonatti.avaliacaofullstack.service.AccountService;
import me.thiagorigonatti.avaliacaofullstack.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/transaction")
public class BankController {

    private final AccountService accountService;

    private final BankService bankService;

    public BankController(AccountService accountService, BankService bankService) {
        this.accountService = accountService;
        this.bankService = bankService;
    }

    @GetMapping("/ofaccount/{number}")
    public ResponseEntity<List<Transaction>> findAllByDepositor(@PathVariable String number) {
        return new ResponseEntity<>(bankService.findAllByDepositor(accountService.findByNumber(number)), HttpStatus.OK);
    }

    @PostMapping("/schedule-transfer")
    public ResponseEntity<Transaction> makeTransaction(@RequestBody @Valid TransferDTOPostBody transferDTOPostBody) {
        return new ResponseEntity<>(bankService.transfer(transferDTOPostBody), HttpStatus.CREATED);
    }

}
