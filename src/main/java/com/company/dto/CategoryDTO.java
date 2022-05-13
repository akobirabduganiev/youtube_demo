package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CategoryDTO implements Serializable {
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer id;

    @NotNull(message = "Name Required!")
    @Size(min = 3, max = 250)
    private String name;
}
