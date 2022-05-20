package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoIdDTO {

    @NotBlank(message = "VideoId required")
    private Integer videoId;

    @NotBlank(message = "PlaylistId required")
    private Integer playlistId;

}
