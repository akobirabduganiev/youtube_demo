package com.company.dto;

import com.company.enums.ChannelStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private ProfileDTO profile;

    public ChannelDTO(String toOpenUrl) {
    }
}
