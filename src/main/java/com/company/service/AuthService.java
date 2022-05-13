package com.company.service;

import com.company.dto.AttachDTO;
import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.AppForbiddenException;
import com.company.exceptions.EmailAlreadyExistsException;
import com.company.exceptions.PasswordOrEmailWrongException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    AttachService attachService;

    public ProfileDTO login(AuthDTO dto) {
        String password = DigestUtils.md5Hex(dto.getPassword());
        Optional<ProfileEntity> optional =
                profileRepository.findByEmailAndPassword(dto.getEmail(), password);
        if (optional.isEmpty()) {
            throw new PasswordOrEmailWrongException("Password or email wrong!");
        }

        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppForbiddenException("No Access.");
        }

        ProfileDTO profile = new ProfileDTO();

        profile.setEmail(entity.getEmail());
        profile.setName(entity.getName());
        profile.setSurname(entity.getSurname());
        profile.setRole(entity.getRole());
        profile.setAttachId(entity.getAttachId());
        profile.setCreatedDate(entity.getCreatedDate());
        profile.setLastModifiedDate(entity.getLastModifiedDate());
        profile.setId(entity.getId());
        profile.setStatus(entity.getStatus());
        profile.setAttachId(entity.getAttachId());
        profile.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));

        // image
        AttachEntity image = entity.getAttach();
        if (image != null) {
            AttachDTO imageDTO = new AttachDTO();
            imageDTO.setUrl(attachService.toOpenURL(image.getId()));
            profile.setImage(imageDTO);
        }
        return profile;
    }


    public void registration(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());

        if (optional.isPresent()) {
            throw new EmailAlreadyExistsException("Email Already Exits");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());

        String password = DigestUtils.md5Hex(dto.getPassword());
        entity.setPassword(password);
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.NOT_CONFIRMED);
        profileRepository.save(entity);

        Thread thread = new Thread(() -> sendVerificationEmail(entity));
        thread.start();
    }

    public void verification(String jwt) {
        Integer userId;
        try {
            userId = JwtUtil.decodeAndGetId(jwt);
        } catch (JwtException e) {
            throw new AppBadRequestException("Verification not completed");
        }
        profileRepository.updateStatus(ProfileStatus.ACTIVE, userId);
    }

    private void sendVerificationEmail(ProfileEntity entity) {
        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.encode(entity.getId());
        builder.append("To verify your registration click to next link.\n\n");
        builder.append("http://localhost:8080/auth/verification/ ").append(jwt);
        emailService.send(entity.getEmail(), "Activate Your Registration", builder.toString());

    }
}
