package com.company.entity;

import com.company.enums.ChannelStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class ChannelEntity extends BaseEntity {
    @Column
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private ChannelStatus status;

    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String key = UUID.randomUUID().toString();

    @Column(name = "banner_photo_id")
    String bannerPhotoId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_photo_id", insertable = false, updatable = false)
    private AttachEntity bannerPhoto;

    @Column(name = "channel_photo_id")
    private String channelPhotoId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_photo_id", insertable = false, updatable = false)
    private AttachEntity channelPhoto;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
}