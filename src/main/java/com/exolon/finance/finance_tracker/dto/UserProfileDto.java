package com.exolon.finance.finance_tracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data // always use data or add getters and setters so that jackson works properly
@Builder
public class UserProfileDto {

    @NotNull
    private String id;
    @NotNull
    private String firstName;
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String country;
    @NotNull
    private String currency;

}
