package com.company.entity;

import com.company.enums.PlaylistStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "playlist")
public class PlaylistEntity extends BaseEntity {
    @Column
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private PlaylistStatus status;
    @Column
    private Integer orderNumber;

    @Column(name = "channel_id")
    private Integer channelId;
    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    ChannelEntity channel;
}