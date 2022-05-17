package com.company.service;

import com.company.dto.LikeDTO;
import com.company.entity.LikeEntity;
import com.company.enums.ProfileRole;
import com.company.exceptions.AppForbiddenException;
import com.company.exceptions.ItemNotFoundException;
import com.company.mapper.VideoLikeDislikeMapper;
import com.company.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    @Lazy
    private VideoService videoService;

    public LikeDTO create(LikeDTO dto, Integer pId) {
        var oldLikeOptional = likeRepository.findByVideoIdAndProfileId(dto.getVideoId(), pId);
        if (oldLikeOptional.isPresent()) {
            oldLikeOptional.get().setStatus(dto.getStatus());
            likeRepository.save(oldLikeOptional.get());
            return dto;
        }

        var entity = new LikeEntity();
        entity.setStatus(dto.getStatus());
        entity.setVideoId(dto.getVideoId());
        entity.setProfileId(pId);
        likeRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    public boolean update(Integer likeId, LikeDTO dto, Integer pId) {
        var entity = get(likeId);

        if (!entity.getProfileId().equals(pId)) {
            throw new AppForbiddenException("Not Access");
        }
        entity.setStatus(dto.getStatus());
        entity.setLastModifiedDate(LocalDateTime.now());
        likeRepository.save(entity);
        return true;
    }

    public boolean delete(Integer likeId, Integer pId, ProfileRole role) {
        var entity = get(likeId);
        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)) {
            likeRepository.delete(entity);
            return true;
        }
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<LikeDTO> listByVideoId(Integer videoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        var pageList = likeRepository.findAllByVideoId(videoId, pageable);

        List<LikeDTO> likeDTOList = new LinkedList<>();
        for (LikeEntity entity : pageList.getContent()) likeDTOList.add(toDTO(entity));
        return new PageImpl<>(likeDTOList, pageable, pageList.getTotalElements());
    }

    public PageImpl<LikeDTO> listByProfileId(Integer profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        var pageList = likeRepository.findAllByProfileId(profileId, pageable);

        List<LikeDTO> likeDTOList = new LinkedList<>();
        for (LikeEntity entity : pageList.getContent())
            likeDTOList.add(toDTO(entity));
        return new PageImpl<>(likeDTOList, pageable, pageList.getTotalElements());
    }

    public PageImpl<LikeDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        var pageList = likeRepository.findAll(pageable);

        List<LikeDTO> likeDTOList = new LinkedList<>();
        for (LikeEntity entity : pageList.getContent()) likeDTOList.add(toDTO(entity));
        return new PageImpl<>(likeDTOList, pageable, pageList.getTotalElements());
    }

    public LikeEntity get(Integer likeId) {
        return likeRepository.findById(likeId).orElseThrow(() -> {
            throw new ItemNotFoundException("Like Not found");
        });
    }

    public LikeDTO getVideoById(Integer videoId, Integer pId) {
        var optional = likeRepository
                .findByVideoIdAndProfileId(videoId, pId);
        if (optional.isPresent()) return toDTO(optional.get());
        return new LikeDTO();
    }

    public LikeDTO getLIkeAndDislikeCount(Integer videoId) {
        VideoLikeDislikeMapper mapper = likeRepository.countArticleLikeAndDislike(videoId);
        LikeDTO dto = new LikeDTO();
        dto.setLikeCount(mapper.getLike_count());
        dto.setDisLikeCount(mapper.getDislike_count());

        return dto;
    }


    public LikeDTO toDTO(LikeEntity entity) {
        LikeDTO dto = new LikeDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setProfileId(entity.getProfileId());
        dto.setVideoId(entity.getVideoId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
