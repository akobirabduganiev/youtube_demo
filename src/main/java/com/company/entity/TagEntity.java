package com.company.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "tag")
public class TagEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}