package com.company.service;

import com.company.dto.ChannelDTO;
import com.company.entity.ChannelEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ChannelStatus;
import com.company.exceptions.AppBadRequestException;
import com.company.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChannelService {
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ProfileService profileService;

    public ChannelDTO create(ChannelDTO dto, Integer pId) {
        ProfileEntity profile = profileService.get(pId);

        ChannelEntity entity = new ChannelEntity();
        entity.setName(dto.getName());
        entity.setStatus(ChannelStatus.ACTIVE);
        entity.setDescription(dto.getDescription());
        entity.setChannelPhotoId(dto.getChannelPhotoId());
        entity.setBannerPhotoId(dto.getBannerPhotoId());
        entity.setProfileId(profile.getId());

        channelRepository.save(entity);
        dto.setKey(entity.getKey());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public ChannelDTO update(ChannelDTO dto, String key, Integer pId) {
        profileService.get(pId);

        ChannelEntity entity = channelRepository.findByProfileIdAndKey(pId, key)
                .orElseThrow(() -> new AppBadRequestException("Not Found!"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setChannelPhotoId(dto.getChannelPhotoId());
        entity.setBannerPhotoId(dto.getBannerPhotoId());
        entity.setLastModifiedDate(LocalDateTime.now());

        channelRepository.save(entity);
        return toDTO(entity);
    }

    private ChannelDTO toDTO(ChannelEntity entity) {
        ChannelDTO dto = new ChannelDTO();
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setChannelPhotoId(entity.getChannelPhotoId());
        dto.setProfileId(entity.getProfileId());
        dto.setName(entity.getName());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setKey(entity.getKey());
        dto.setStatus(entity.getStatus());
        dto.setBannerPhotoId(entity.getBannerPhotoId());

        return dto;
    }
}
