package com.company.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tag")
public class TagEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}