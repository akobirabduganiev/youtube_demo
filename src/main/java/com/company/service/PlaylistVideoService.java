package com.company.service;

import com.company.dto.*;
import com.company.entity.PlaylistEntity;
import com.company.entity.PlaylistVideoEntity;
import com.company.entity.VideoEntity;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.AppForbiddenException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.PlaylistRepository;
import com.company.repository.PlaylistVideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistVideoService {

    private final PlaylistVideoRepository playlistVideoRepository;
    private final PlaylistRepository playlistRepository;
    private final VideoService videoService;
    private final ChannelService channelService;


    public PlaylistVideoDTO create(PlaylistVideoDTO dto, String profileId) {
        VideoEntity videoEntity = videoService.getById(dto.getVideoId());

        PlaylistEntity playlistEntity = getPlaylistById(dto.getPlaylistId());

        if (!playlistEntity.getChannel().getProfileId().toString().equals(profileId)) {
            log.warn("Not access {}", profileId);
            throw new AppForbiddenException("Not access!");
        }

        PlaylistVideoEntity entity = new PlaylistVideoEntity();
        entity.setPlaylistId(playlistEntity.getId());
        entity.setVideoId(videoEntity.getId());
        entity.setOrderNum(dto.getOrderNum());

        playlistVideoRepository.save(entity);

        entity.setVideo(videoEntity);
        entity.setPlaylist(playlistEntity);
        return toDTO(entity);
    }

    public PlaylistVideoDTO update(UpdateOrderNumDTO dto, Integer playlistVideoId, Integer profileId) {
        PlaylistVideoEntity entity = getById(playlistVideoId);

        if (!entity.getPlaylist().getChannel().getProfileId().equals(profileId)) {
            log.warn("Not access {}", profileId);
            throw new AppForbiddenException("Not access!");
        }

        entity.setOrderNum(dto.getOrderNum());
        entity.setLastModifiedDate(LocalDateTime.now());

        playlistVideoRepository.save(entity);

        return toDTO(entity);
    }

    public Boolean delete(PlaylistVideoIdDTO dto, String profileId) {
        VideoEntity videoEntity = videoService.getById(dto.getVideoId());

        PlaylistEntity playlistEntity = getPlaylistById(dto.getPlaylistId());

        if (!playlistEntity.getChannel().getProfileId().toString().equals(profileId)) {
            log.warn("Not access {}", profileId);
            throw new AppForbiddenException("Not access!");
        }

        PlaylistVideoEntity entity = getByPlaylistIdAndVideoId(dto.getPlaylistId(), dto.getVideoId());

        playlistVideoRepository.delete(entity);
        return true;
    }

    public List<PlaylistVideoDTO> videosByPlaylistId(Integer playlistId) {
        PlaylistEntity playlistEntity = getPlaylistById(playlistId);

        List<PlaylistVideoDTO> dtoList = new ArrayList<>();

        List<PlaylistVideoEntity> entityList = playlistVideoRepository.findAllByPlaylistId(playlistEntity.getId(),
                Sort.by(Sort.Direction.ASC, "orderNum"));

        entityList.forEach(entity -> {
            dtoList.add(toDTO(entity));
        });
        return dtoList;
    }

    public PlaylistVideoDTO get(Integer playlistVideoId) {
        return toDTO(getById(playlistVideoId));
    }

    public PlaylistVideoEntity getByPlaylistIdAndVideoId(Integer playlistId, Integer videoId) {
        return playlistVideoRepository
                .findByPlaylistIdAndVideoId(playlistId, videoId)
                .orElseThrow(() -> {
                    log.warn("Not found playlistId={} videoId={}", playlistId, videoId);
                    return new AppBadRequestException("Not found!");
                });
    }


    public PlaylistEntity getPlaylistById(Integer id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Not found {}", id);
                    throw new ItemNotFoundException("Not found!");
                });
    }

    public PlaylistVideoEntity getById(Integer id) {
        return playlistVideoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Not found {}", id);
                    throw new ItemNotFoundException("Not found!");
                });

    }

    public PlaylistVideoDTO toDTO(PlaylistVideoEntity entity) {
        PlaylistVideoDTO dto = new PlaylistVideoDTO();
        dto.setId(entity.getId());
        dto.setPlaylistId(entity.getPlaylistId());

        VideoEntity videoEntity = entity.getVideo();
        dto.setVideo(new VideoDTO(videoEntity.getId(),
                videoEntity.getTitle(),
                videoEntity.getDescription(),
                new AttachDTO(videoService.toOpenUrl(entity.getVideoId().toString())),
                videoEntity.getDuration()));

        dto.setChannel(new ChannelDTO(channelService.toOpenUrl(entity.getVideo().getChannelId().toString())));

        dto.setOrderNum(entity.getOrderNum());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        return dto;
    }


}
