package com.company.dto;

import com.company.enums.PlaylistStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePlaylistStatusDTO {
    private Integer channelId;
    private PlaylistStatus status;
    private Integer playlistId;
}
