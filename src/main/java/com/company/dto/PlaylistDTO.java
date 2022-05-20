package com.company.dto;

import com.company.enums.PlaylistStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class PlaylistDTO implements Serializable {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    @NotNull(message = "name required!")
    private String name;
    @NotNull(message = "description required!")
    private String description;
    @NotNull(message = "status required!")
    private PlaylistStatus status;
    @NotNull(message = "orderNumber required!")
    private Integer orderNumber;
    @NotNull(message = "channelId required!")
    private Integer channelId;
    private Integer videoCount;
    private List<VideoDTO> videoList;
    private ChannelDTO channel;
}
