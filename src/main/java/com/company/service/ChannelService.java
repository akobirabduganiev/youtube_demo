package com.company.service;

import com.company.dto.ChangeBannerDTO;
import com.company.dto.ChangeProfilePhotoDTO;
import com.company.dto.ChangeChannelStatusDTO;
import com.company.dto.ChannelDTO;
import com.company.entity.ChannelEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ChannelStatus;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelService {
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    AttachService attachService;

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

    public boolean updateImage(ChangeProfilePhotoDTO dto, Integer pId) {

        attachService.get(dto.getPhotoId());
        var entity = channelRepository.findByProfileIdAndKey(pId, dto.getChannelKey())
                .orElseThrow(() -> new AppBadRequestException("Not Found!"));

        if (entity.getChannelPhoto() != null) {
            attachService.delete(entity.getChannelPhoto().getId());
        }
        channelRepository.updateChannelPhoto(dto.getPhotoId(), dto.getChannelKey());
        channelRepository.updateLastModifiedDate(LocalDateTime.now(), dto.getChannelKey());

        return true;
    }

    public boolean updateBanner(ChangeBannerDTO dto, Integer pId) {
        attachService.get(dto.getPhotoId());

        var entity = channelRepository.findByProfileIdAndKey(pId, dto.getChannelKey())
                .orElseThrow(() -> new AppBadRequestException("Not Found!"));

        if (entity.getBannerPhoto() != null) {
            attachService.delete(entity.getBannerPhoto().getId());
        }
        channelRepository.updateBannerPhoto(dto.getPhotoId(), dto.getChannelKey());
        channelRepository.updateLastModifiedDate(LocalDateTime.now(), dto.getChannelKey());

        return true;
    }

    public boolean updateStatus(ChangeChannelStatusDTO dto, Integer pId) {
        channelRepository.findByProfileIdAndKey(pId, dto.getKey())
                .orElseThrow(() -> new AppBadRequestException("Not Found!"));

        channelRepository.updateChannelStatus(dto.getStatus(), dto.getKey());
        channelRepository.updateLastModifiedDate(LocalDateTime.now(), dto.getKey());

        return true;
    }

    public List<ChannelDTO> paginationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ChannelDTO> list = channelRepository.findAll(pageable).stream().map(this::toDTO).toList();
        if (list.isEmpty()) throw new ItemNotFoundException("Channel List is empty!");

        return list;
    }

    public List<ChannelDTO> channelList(Integer pId) {

        List<ChannelEntity> list = channelRepository.findByProfileId(pId);
        if (list.isEmpty()) throw new ItemNotFoundException("Channel List is empty!");

        List<ChannelDTO> channelList = new ArrayList<>();
        for (ChannelEntity entity : list)
            channelList.add(toDTO(entity));

        return channelList;
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

    public ChannelEntity get(String key) {
        return channelRepository.findByKey(key).orElseThrow(() -> new ItemNotFoundException("Not Found!"));
    }
}
