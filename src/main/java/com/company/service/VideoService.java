package com.company.service;

import com.company.dto.*;
import com.company.entity.VideoEntity;
import com.company.enums.VideoStatus;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ChannelService channelService;

    @Value("${server.domain.name}")
    private String domainName;


    public VideoDTO create(VideoDTO dto, Integer pId) {
        var entity = new VideoEntity();
        var channel = channelService.get(dto.getChannelId());
        attachService.get(dto.getPreviewAttachId());
        attachService.get(dto.getVideoAttachId());

        if (!channel.getProfileId().equals(pId)) throw new AppBadRequestException("Not authorized!");

        if (!dto.getStatus().equals(VideoStatus.NOT_PUBLISHED))
            entity.setPublishedDate(LocalDateTime.now());

        entity.setStatus(dto.getStatus());
        entity.setChannelId(dto.getChannelId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        entity.setVideoAttachId(dto.getVideoAttachId());

        videoRepository.save(entity);
        dto.setKey(entity.getKey());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public List<ListVideoDetailDTO> paginationList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        return videoRepository.findAll(pageable).stream().map(this::videoList).toList();
    }

    public String increaseViewCount(String key) {
        videoRepository.increaseViewCount(key);
        return "view count increased";
    }

    public String updateDetail(ChangeVideoDetailDTO dto, Integer pId) {
        var entity = videoRepository.findByKey(dto.getKey()).orElseThrow(() -> new ItemNotFoundException("Video not found!"));
        var channel = channelService.get(dto.getChannelKey());
        if (!channel.getProfileId().equals(pId)) throw new AppBadRequestException("Not authorized!");
        if (!entity.getChannelId().equals(channel.getId())) throw new AppBadRequestException("Not access!");

        videoRepository.updateVideoDetail(dto.getTitle(), dto.getDescription(), dto.getKey());

        return "Video detail updated successfully!";
    }

    public String changeVideoStatus(ChangeVideoStatusDTO dto, Integer pId) {
        var entity = videoRepository.findByKey(dto.getKey()).orElseThrow(() -> new ItemNotFoundException("Video not found!"));
        var channel = channelService.get(dto.getChannelKey());
        if (!channel.getProfileId().equals(pId)) throw new AppBadRequestException("Not access!");
        if (!entity.getChannelId().equals(channel.getId())) throw new AppBadRequestException("Not access!");

        videoRepository.changeVideoStatus(dto.getStatus(), dto.getKey());

        return "Video status changed successfully!";
    }

    public PageImpl<VideoDetailDTO> listByCategory(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        var pageList = videoRepository.findAllByCategoryId(categoryId, pageable);

        List<VideoDetailDTO> list = new LinkedList<>();
        for (VideoEntity entity : pageList.getContent())
            list.add(shortVideoDetail(entity));
        return new PageImpl<>(list, pageable, pageList.getTotalElements());
    }

    public PageImpl<VideoDetailDTO> listByTitle(VideoTitleDTO dto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        var pageList = videoRepository.findAllByTitle(dto.getTitle(), pageable);

        List<VideoDetailDTO> list = new LinkedList<>();
        for (VideoEntity entity : pageList.getContent()) list.add(shortVideoDetail(entity));

        return new PageImpl<>(list, pageable, pageList.getTotalElements());
    }

    public VideoDetailDTO getVideoByKey(String key, Integer pId) {
        var entity = videoRepository.findByKey(key).orElseThrow(() -> new ItemNotFoundException("Video not found!"));
        VideoDetailDTO dto = null;

        if (!entity.getStatus().equals(VideoStatus.PRIVATE)) dto = fullVideoDetail(entity);

        List<ChannelDTO> list = channelService.channelList(pId);
        for (ChannelDTO channel : list)
            if (channel.getProfileId().equals(pId)) dto = fullVideoDetail(entity);

        return dto;
    }


    private VideoDetailDTO shortVideoDetail(VideoEntity entity) {
        var dto = new VideoDetailDTO();
        dto.setKey(entity.getKey());
        var channel = new ChannelDTO();
        channel.setName(entity.getChannel().getName());
        channel.setKey(entity.getChannel().getKey());
        channel.setChannelPhotoId(attachService.toOpenURL(entity.getChannel().getChannelPhotoId()));
        dto.setChannel(channel);
        dto.setPreviewAttach(attachService.toOpenURL(entity.getPreviewAttachId()));
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setVideoAttach(attachService.toOpenURL(entity.getVideoAttachId()));
        dto.setViewCount(entity.getViewCount());
        dto.setCategoryId(entity.getCategoryId());

        return dto;
    }

    private VideoDetailDTO fullVideoDetail(VideoEntity entity) {
        var dto = new VideoDetailDTO();
        dto.setKey(entity.getKey());
        var channel = new ChannelDTO();
        channel.setName(entity.getChannel().getName());
        channel.setKey(entity.getChannel().getKey());
        channel.setChannelPhotoId(attachService.toOpenURL(entity.getChannel().getChannelPhotoId()));
        dto.setChannel(channel);
        dto.setPreviewAttach(attachService.toOpenURL(entity.getPreviewAttachId()));
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setVideoAttach(attachService.toOpenURL(entity.getVideoAttachId()));
        dto.setViewCount(entity.getViewCount());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setSharedCount(entity.getSharedCount());
        dto.setCategoryId(entity.getCategoryId());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    private ListVideoDetailDTO videoList(VideoEntity entity) {
        var dto = new ListVideoDetailDTO();
        dto.setVideo(shortVideoDetail(entity));

        return dto;
    }


    public VideoEntity getById(Integer videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new ItemNotFoundException("Item not found"));
    }
    public String toOpenUrl(String id) {
        return domainName + "video/public/" + id;
    }
}
