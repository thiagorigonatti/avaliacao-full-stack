package me.thiagorigonatti.avaliacaofullstack.mapper;

import me.thiagorigonatti.avaliacaofullstack.domain.Account;
import me.thiagorigonatti.avaliacaofullstack.domain.User;
import me.thiagorigonatti.avaliacaofullstack.dto.AccountDTOPostBody;

import javax.validation.Valid;

public enum AccountMapper {

    INSTANCE;

    public Account toAccount(AccountDTOPostBody accountDTOPostBody) {

        if (accountDTOPostBody == null) return null;

        User user = new User();
        user.setName(accountDTOPostBody.getName());
        user.setEmail(accountDTOPostBody.getEmail());
        user.setSsn(accountDTOPostBody.getSsn());

        Account account = new Account();
        String number = String.valueOf(user.hashCode()).substring(0, 6);
        account.setNumber(number);
        account.setUser(user);

        return account;

    }
}
