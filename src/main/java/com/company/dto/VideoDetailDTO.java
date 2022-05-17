package com.company.dto;

import com.company.enums.VideoStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class VideoDetailDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String title;
    private LocalDateTime publishedDate;
    private VideoStatus status;
    private String key;
    private String description;
    private Integer viewCount;
    private Integer sharedCount;
    private ChannelDTO channel;
    private String  previewAttach;
    private String  videoAttach;
    private Integer categoryId;
}
