package com.company.dto;

import com.company.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
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
    private String url;
    private Long duration;

    public VideoDTO(String url) {
        this.url = url;
    }

    public VideoDTO(Integer id, String title, String description, AttachDTO video, Long duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoAttachId = video.getUrl();
        this.duration = duration;
    }

}
