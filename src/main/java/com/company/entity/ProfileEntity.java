package com.company.entity;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseEntity {
    @Column
    private String name;
    @Column
    private String surname;
    @Column(unique = true)
    private String email;
    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    ProfileStatus status;

    @Enumerated(EnumType.STRING)
    @Column
    ProfileRole role;

    @Column(name = "profile_photo_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_photo_id", insertable = false, updatable = false)
    @ToString.Exclude
    private AttachEntity attach;
}