package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @NotNull(message = "VideoId required")
    private Integer videoId;
    private VideoDTO video;

    @NotNull(message = "PlaylistId required")
    private Integer playlistId;
    private PlaylistDTO playlist;

    @NotNull(message = "OrderNum required")
    @Positive(message = "Invalid OrderNum")
    private Integer orderNum;

    private ChannelDTO channel;

}
