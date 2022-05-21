package com.company.repository;

import com.company.entity.VideoTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoTagRepository extends JpaRepository<VideoTagEntity, Integer> {

    Optional<VideoTagEntity> findByTagIdAndVideoId(Integer tagId, Integer videoId);
}