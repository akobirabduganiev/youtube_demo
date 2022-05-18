package com.company.dto;

import com.company.enums.VideoStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@Setter
public class VideoDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    @NotNull(message = "Title required!")
    private String title;
    private LocalDateTime publishedDate;
    @NotNull(message = "Status required!")
    private VideoStatus status;
    private String key;
    @NotNull(message = "Description required!")
    private String description;
    private Integer viewCount;
    private Integer sharedCount;
    @NotNull(message = "channelId required!")
    private Integer channelId;
    @NotNull(message = "previewAttachId required!")
    private String previewAttachId;
    @NotNull(message = "VideoAttachId required!")
    private String videoAttachId;
    @NotNull(message = "CategoryId required!")
    private Integer categoryId;
}
