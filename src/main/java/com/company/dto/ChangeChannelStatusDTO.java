package com.company.dto;

import com.company.enums.ChannelStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeChannelStatusDTO {
    @NotNull(message = "Status required!")
    ChannelStatus status;
    @NotNull(message = "Key required!")
    String key;
}
