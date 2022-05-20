package com.company.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class PlaylistVideoDTO implements Serializable {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer videoId;
    private Integer playlistId;
    private Integer orderNum;
    private VideoDTO video;
    private ChannelDTO channel;
}
