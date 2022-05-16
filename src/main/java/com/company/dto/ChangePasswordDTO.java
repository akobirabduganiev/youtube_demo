package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordDTO {
    @NotNull(message = "Old password required!")
    private String oldPassword;
    @NotNull(message = "New password required!")
    private String newPassword;
}
