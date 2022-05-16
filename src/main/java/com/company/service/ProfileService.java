package com.company.service;

import com.company.dto.*;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exceptions.*;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private EmailService emailService;

    public ProfileDTO create(ProfileDTO dto, Integer pId) {
        ProfileEntity profile = get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            log.warn("Not access {}", pId);
            throw new AppForbiddenException("Not access");
        }
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email already exists {}", dto.getEmail());
            throw new EmailAlreadyExistsException("Email Already Exits");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());

        String password = DigestUtils.md5Hex(dto.getPassword());
        entity.setPassword(password);

        if (dto.getRole() == null)
            entity.setRole(ProfileRole.USER);
        else
            entity.setRole(dto.getRole());

        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setRole(entity.getRole());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public ProfileDTO getById(Integer id, Integer pId) {
        ProfileEntity profile = get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            log.warn("Not access {}", pId);
            throw new AppForbiddenException("Not access");
        }
        ProfileEntity entity = profileRepository.findById(id).orElseThrow(() -> {
            log.warn("Not found {}", id);
            return new ItemNotFoundException("Profile not Found!");
        });
        return toDTO(entity);
    }

    public ProfileDTO update(Integer id, ProfileDTO dto, Integer pId) {
        ProfileEntity profile = get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            log.warn("Not access {}", pId);
            throw new AppForbiddenException("Not access");
        }
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email already used {}", dto.getEmail());
            throw new EmailAlreadyExistsException("This Email already used!");
        }

        ProfileEntity entity = profileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());

        String password = DigestUtils.md5Hex(dto.getPassword());
        entity.setPassword(password);

        entity.setLastModifiedDate(LocalDateTime.now());
        profileRepository.save(entity);

        return toDTO(entity);
    }

    public Boolean delete(Integer id, Integer pId) {
        ProfileEntity profile = get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            log.warn("Not access {}", pId);
            throw new AppForbiddenException("Not access");
        }
        profileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));
        profileRepository.deleteById(id);
        return true;
    }

    public boolean updateImage(ChangeProfileImageDTO dto, Integer pId) {
        ProfileEntity profileEntity = get(pId);

        if (profileEntity.getAttach() != null) {
            attachService.delete(profileEntity.getAttach().getId());
        }
        profileRepository.updateAttach(dto.getAttachId(), pId);
        updateLastModifiedDate(pId);

        return true;
    }

    public String updateProfileDetail(ChangeProfileDetailDTO dto, Integer pId) {

        get(pId);
        profileRepository.updateProfileDetail(dto.getName(), dto.getSurname(), pId);
        updateLastModifiedDate(pId);

        return "Profile updated successfully!";
    }

    public String changePassword(ChangePasswordDTO dto, Integer pId) {
        get(pId);
        Boolean password = profileRepository.changePassword(DigestUtils.md5Hex(dto.getNewPassword()),
                DigestUtils.md5Hex(dto.getOldPassword()), pId);
        if (!password)
            throw new PasswordOrEmailWrongException("Your password is wrong!");
        updateLastModifiedDate(pId);

        return "Password changed successfully!";
    }

    public String changeEmail(ChangeProfileEmailDTO dto, Integer pId) {
        get(pId);
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email already exists {}", dto.getEmail());
            throw new EmailAlreadyExistsException("Email Already Exits");
        }
        sendVerificationEmail(dto, pId);

        return "email verification not completed! please confirm your email";
    }

    private ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setAttachId(entity.getAttachId());
        return dto;
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            log.warn("profile not found {}", id);
            throw new ItemNotFoundException("Profile not found!");
        });
    }

    private void updateLastModifiedDate(Integer id) {
        profileRepository.updateLastModifiedDate(LocalDateTime.now(), id);
    }

    private void sendVerificationEmail(ChangeProfileEmailDTO dto, Integer id) {

        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.encode(id, dto.getEmail());
        builder.append("To verify your registration click to next link.\n\n");
        builder.append("http://localhost:8080/auth/verification/email/").append(jwt);
        emailService.send(dto.getEmail(), "Activate Your Registration", builder.toString());
    }

    public String verification(String jwt) {
        ProfileJwtDTO dto;
        try {
            dto = JwtUtil.decode(jwt);
        } catch (JwtException e) {
            throw new AppBadRequestException("Verification not completed!");
        }
        profileRepository.changeEmail(dto.getEmail(), dto.getId());

        return "Your email confirmed!";
    }
}
