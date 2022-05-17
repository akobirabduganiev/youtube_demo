package com.company.entity;

import com.company.enums.VideoStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "video")
public class VideoEntity extends BaseEntity {
    @Column
    private String title;
    @Column
    private LocalDateTime publishedDate;
    @Column
    @Enumerated(EnumType.STRING)
    VideoStatus status;
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String key = UUID.randomUUID().toString();
    @Column(columnDefinition = "text")
    private String description;
    @Column
    private Integer viewCount;
    @Column
    private Integer sharedCount;

    @Column(name = "channel_id")
    private Integer channelId;
    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @ManyToOne
    @JoinColumn(name = "preview_attach_id", insertable = false, updatable = false)
    private AttachEntity previewAttach;

    @Column(name = "video_attach_id")
    private String videoAttachId;
    @ManyToOne
    @JoinColumn(name = "video_attach_id", insertable = false, updatable = false)
    private AttachEntity videoAttach;

    @Column(name = "category_id")
    private Integer categoryId;
    @OneToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;
}