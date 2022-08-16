package me.thiagorigonatti.avaliacaofullstack.controller;

import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import me.thiagorigonatti.avaliacaofullstack.dto.AccountDTOPostBody;
import me.thiagorigonatti.avaliacaofullstack.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAll() {
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/find/{number}")
    public ResponseEntity<Account> findByNumber(@PathVariable String number) {
        return new ResponseEntity<>(accountService.findByNumber(number), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Account> save(@RequestBody @Valid AccountDTOPostBody accountDTOPostBody) {
        return new ResponseEntity<>(accountService.save(accountDTOPostBody), HttpStatus.CREATED);
    }
}
