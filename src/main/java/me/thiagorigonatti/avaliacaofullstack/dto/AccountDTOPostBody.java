package me.thiagorigonatti.avaliacaofullstack.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AccountDTOPostBody {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String ssn;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSsn() {
        return ssn;
    }
}
