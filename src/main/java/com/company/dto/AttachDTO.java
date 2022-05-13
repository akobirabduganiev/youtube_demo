package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttachDTO implements Serializable {
    private String url;
    private String id;
    private String path;
    private String extension;
    private String originName;
    private Long size;
    private LocalDateTime createdDate;

    public AttachDTO() {

    }

    public AttachDTO(String url) {
        this.url = url;
    }
}
