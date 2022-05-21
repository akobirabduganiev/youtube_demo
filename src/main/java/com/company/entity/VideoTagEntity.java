package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "video_tag")
public class VideoTagEntity extends BaseEntity {
    @Column(name = "video_id", unique = true)
    Integer videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    VideoEntity video;

    @Column(name = "tag_id", unique = true)
    Integer tagId;
    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    TagEntity tag;
}