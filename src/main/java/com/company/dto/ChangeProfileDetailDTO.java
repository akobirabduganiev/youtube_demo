package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeProfileDetailDTO {
    @NotNull(message = "Name Required!")
    private String name;
    @NotNull(message = "Surname Required!")
    private String surname;

}
