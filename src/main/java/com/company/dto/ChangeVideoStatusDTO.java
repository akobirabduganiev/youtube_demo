package com.company.dto;

import com.company.enums.VideoStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeVideoStatusDTO {
    private String key;
    private String channelKey;
    private VideoStatus status;
}
