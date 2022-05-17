package com.company.repository;

import com.company.entity.LikeEntity;
import com.company.entity.VideoEntity;
import com.company.enums.VideoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
    Optional<VideoEntity> getByKey(String key);

    Page<VideoEntity> findAllByCategoryId(Integer categoryId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update VideoEntity set title=:title, description=:description where key=:key")
    void updateVideoDetail(@Param("title") String title, @Param("description") String description, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update VideoEntity set status=:status where key=:key")
    void changeVideoStatus(@Param("status") VideoStatus status, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update VideoEntity set viewCount=viewCount+1 where key=:key")
    void increaseViewCount(@Param("key") String key);
}