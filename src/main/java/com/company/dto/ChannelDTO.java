package com.company.dto;

import com.company.enums.ChannelStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class ChannelDTO implements Serializable {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String name;
    private String description;
    private ChannelStatus status;
    private String key;
    private String bannerPhotoId;
    private String channelPhotoId;
    private Integer profileId;
}
