package com.company.dto;

import com.company.enums.LikeStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class LikeDTO implements Serializable {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private LikeStatus status;
    private Integer profileId;
    private Integer videoId;

    private Integer likeCount;
    private Integer disLikeCount;
}
