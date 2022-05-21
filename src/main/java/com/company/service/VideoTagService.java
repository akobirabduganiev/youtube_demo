package com.company.service;

import com.company.dto.VideoTagDTO;
import com.company.entity.VideoTagEntity;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.repository.VideoTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoTagService {
    private final VideoTagRepository videoTagRepository;
    private final VideoService videoService;
    private final TagService tagService;

    public VideoTagDTO create(VideoTagDTO dto, Integer pId) {
        var video = videoService.getById(dto.getVideoId());

        if (video.getChannel().getProfileId().equals(pId))
            throw new AppBadRequestException("This channel not for you!");
        var tag = tagService.getById(dto.getTagId());

        var optional = videoTagRepository.findByTagIdAndVideoId(tag.getId(), video.getId());
        if (optional.isPresent()) throw new ItemAlreadyExistsException("This tag already exists!");

        var entity = new VideoTagEntity();
        entity.setVideoId(dto.getVideoId());
        entity.setTagId(dto.getTagId());

        videoTagRepository.save(entity);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());

        return dto;
    }

    public String delete(Integer videoId, Integer tagId) {

        return null;
    }
}
