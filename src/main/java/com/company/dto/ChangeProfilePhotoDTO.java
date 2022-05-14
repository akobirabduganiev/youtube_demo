package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeProfilePhotoDTO {
    @NotNull(message = "photo id required!")
    String PhotoId;
    @NotNull(message = "channel key required!")
    String channelKey;
}
