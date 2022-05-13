package com.company.dto;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class ProfileDTO implements Serializable {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    @NotNull(message = "Name Required!")
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotNull(message = "Surname Required!")
    @NotBlank
    @Size(min = 3)
    private String surname;
    @NotNull(message = "Email Required!")
    @NotBlank
    @Size(min = 3)
    @Email(message = "Enter valid email!")
    private String email;
    @NotNull(message = "Password Required!")
    @NotBlank
    @Size(min = 5)
    private String password;
    private ProfileStatus status;
    private ProfileRole role;
    private String attachId;
    private AttachDTO image;
    private String jwt;

}
