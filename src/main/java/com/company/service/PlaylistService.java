package com.company.service;

import com.company.dto.*;
import com.company.entity.ChannelEntity;
import com.company.entity.PlaylistEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.PlaylistStatus;
import com.company.enums.ProfileRole;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.mapper.PlayListInfoAdminMapper;
import com.company.repository.PlaylistRepository;
import com.company.repository.PlaylistVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final ProfileService profileService;
    private final PlaylistVideoRepository playlistVideoRepository;
    private final PlaylistRepository playlistRepository;
    private final ChannelService channelService;
    private final AttachService attachService;

    public PlaylistDTO create(PlaylistDTO dto, Integer pId) {
        ChannelEntity channel = channelService.get(dto.getChannelId());
        if (!channel.getProfileId().equals(pId)) throw new AppBadRequestException("this channel is not for you");

        Optional<PlaylistEntity> optional = playlistRepository.findByChannelIdAndName(dto.getChannelId(), dto.getName());
        if (optional.isPresent()) throw new ItemAlreadyExistsException("This playlist already created!");

        var entity = new PlaylistEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setChannelId(dto.getChannelId());

        playlistRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public String update(ChangePlaylistDTO dto, Integer pId) {
        ChannelEntity channel = channelService.get(dto.getChannelId());
        if (!channel.getProfileId().equals(pId)) throw new AppBadRequestException("this channel is not for you");

        playlistRepository.findById(dto.getPlaylistId()).orElseThrow(() -> new ItemNotFoundException("Playlist not found!"));
        playlistRepository.updatePlaylistDetail(dto.getTitle(), dto.getDescription(), dto.getPlaylistId());
        playlistRepository.updateLastModifiedDate(LocalDateTime.now(), dto.getPlaylistId());

        return "Updated successfully";
    }

    public String changeStatus(ChangePlaylistStatusDTO dto, Integer pId) {
        ChannelEntity channel = channelService.get(dto.getChannelId());
        if (!channel.getProfileId().equals(pId)) throw new AppBadRequestException("this channel is not for you");

        playlistRepository.findById(dto.getPlaylistId()).orElseThrow(() -> new ItemNotFoundException("Playlist not found!"));
        playlistRepository.changeStatus(dto.getStatus(), dto.getPlaylistId());
        playlistRepository.updateLastModifiedDate(LocalDateTime.now(), dto.getPlaylistId());

        return "Updated successfully";
    }

    public String deleteById(Integer playlistId, Integer pId) {
        PlaylistEntity entity = playlistRepository.findById(playlistId).orElseThrow(() -> new ItemNotFoundException("Playlist not found!"));

        if (profileService.get(pId).getRole().equals(ProfileRole.ADMIN)) playlistRepository.delete(entity);
        channelService.findByProfileId(entity.getChannelId(), pId);
        playlistRepository.delete(entity);

        return "Delete successfully";
    }

    public PageImpl<PlaylistDTO> admPlaylist(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PlayListInfoAdminMapper> entityPage = playlistRepository.getPlaylistInfoForAdmin(pageable);

        List<PlayListInfoAdminMapper> entityList = entityPage.getContent();
        List<PlaylistDTO> playListDTO = new LinkedList<>();
        entityList.forEach(entity -> {
            var dto = new PlaylistDTO();
            dto.setId(entity.getPl_id());
            dto.setName(entity.getPl_name());
            dto.setDescription(entity.getPl_description());
            dto.setStatus(PlaylistStatus.valueOf(entity.getPl_status()));

            var channelDTO = new ChannelDTO();
            channelDTO.setId(entity.getCh_id());
            channelDTO.setName(entity.getCh_name());
            if (entity.getCh_photo_id() != null) {
                var attachDTO = new AttachDTO(attachService.toOpenURL(entity.getCh_photo_id()));
                channelDTO.setChannelPhotoId(attachDTO.getId());
            }

            dto.setChannel(channelDTO);

            var profileDTO = new ProfileDTO();
            profileDTO.setId(entity.getPr_id());
            profileDTO.setName(entity.getPr_name());
            profileDTO.setSurname(entity.getPr_surname());

            if (Optional.ofNullable(entity.getPr_photo_id()).isPresent()) {
                AttachDTO attachDTO = new AttachDTO(attachService.toOpenURL(entity.getPr_photo_id()));
                profileDTO.setImage(attachDTO);
            }
            channelDTO.setProfile(profileDTO);

            playListDTO.add(dto);
        });
        return new PageImpl<>(playListDTO, pageable, entityPage.getTotalElements());
    }
    public List<PlaylistDTO> channelPlaylist(String channelId) {
        var channelEntity = channelService.get(channelId);

        List<PlaylistDTO> dtoList = new ArrayList<>();

        List<PlaylistEntity> entityList = playlistRepository.findAllByChannelIdAndStatus(channelEntity.getId(),
                PlaylistStatus.PUBLIC,
                Sort.by(Sort.Direction.DESC, "orderNumber"));

        entityList.forEach(entity -> {
            dtoList.add(toShortDTO(entity));
        });
        return dtoList;
    }

    public List<PlaylistDTO> profilePlaylist(Integer profileId) {
        ProfileEntity profileEntity = profileService.get(profileId);

        List<PlaylistDTO> dtoList = new ArrayList<>();

        List<PlaylistEntity> entityList = playlistRepository.findAllByProfileId(profileEntity.getId(),
                Sort.by(Sort.Direction.DESC, "orderNumber"));

        entityList.forEach(entity -> {
            dtoList.add(toShortDTO(entity));
        });
        return dtoList;
    }

    public PlaylistDTO get(Integer playlistId) {
        PlaylistEntity entity = getById(playlistId);
        return toShortDTO(entity);
    }

    public PlaylistEntity getById(Integer id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> {
                    throw new ItemNotFoundException("Not found!");
                });
    }

    public PlaylistDTO toShortDTO(PlaylistEntity entity) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        ChannelDTO channel = new ChannelDTO(channelService.toOpenUrl(entity.getChannel().getKey()));
        dto.setChannelId(channel.getId());

        dto.setVideoCount(playlistVideoRepository.getVideoCountByPlaylistId(entity.getId()));


        List<VideoDTO> videoList = playlistVideoRepository.getTop2VideoByPlaylistId(entity.getId())
                .stream()
                .map(playlistVideoEntity -> {
                    var videoDTO = new VideoDTO();
                    videoDTO.setId(playlistVideoEntity.getVideoId());
                    videoDTO.setTitle(playlistVideoEntity.getVideo().getTitle());
                    videoDTO.setDuration(playlistVideoEntity.getVideo().getDuration());
                    return videoDTO;
                }).toList();

        dto.setVideoList(videoList);
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

}
