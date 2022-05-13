package com.company.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
}