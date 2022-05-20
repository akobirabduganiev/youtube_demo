package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "playlist_video", uniqueConstraints = @UniqueConstraint(columnNames = {"video_id", "playlist_id"}))
public class PlaylistVideoEntity extends BaseEntity {

    @Column(name = "video_id", nullable = false)
    private Integer videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "playlist_id", nullable = false)
    private Integer playlistId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", insertable = false, updatable = false)
    private PlaylistEntity playlist;

    @Column(name = "order_num", nullable = false)
    private Integer orderNum;
}

