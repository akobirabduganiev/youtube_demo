package com.company.service;

import com.company.dto.ChangeProfileDetailDTO;
import com.company.dto.ChangeProfileImageDTO;
import com.company.dto.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exceptions.AppForbiddenException;
import com.company.exceptions.EmailAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;

    public ProfileDTO create(ProfileDTO dto, Integer pId) {
        ProfileEntity profile = get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            throw new AppForbiddenException("Not access");
        }
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
            throw new AppForbiddenException("Not access");
        }
        ProfileEntity entity = profileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));
        return toDTO(entity);
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));
    }

    public ProfileDTO update(Integer id, ProfileDTO dto, Integer pId) {
        ProfileEntity profile = get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            throw new AppForbiddenException("Not access");
        }
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
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

    public String updateProfileDetail(ChangeProfileDetailDTO dto, Integer pId) {

        profileRepository.findById(pId).orElseThrow(() -> new ItemNotFoundException("Profile not found!"));
        profileRepository.updateProfileDetail(dto.getName(), dto.getSurname(), pId);
        profileRepository.updateLastModifiedDate(LocalDateTime.now(), pId);

        return "Profile updated successfully!";
    }

    public Boolean delete(Integer id, Integer pId) {
        ProfileEntity profile = get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
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
        profileRepository.updateLastModifiedDate(LocalDateTime.now(), pId);

        return true;
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
}
