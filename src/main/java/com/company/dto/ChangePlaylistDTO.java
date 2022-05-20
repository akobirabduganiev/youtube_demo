package com.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePlaylistDTO {
    private String description;
    private String title;
    private Integer playlistId;
    private Integer channelId;
}
