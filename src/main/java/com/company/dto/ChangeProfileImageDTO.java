package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeProfileImageDTO {
    @NotNull(message = "AttachId required!")
    String attachId;
}
