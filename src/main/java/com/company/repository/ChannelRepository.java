package com.company.repository;

import com.company.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Integer> {
    Optional<ChannelEntity> findByProfileIdAndKey(Integer pId, String key);

    Optional<ChannelEntity> findByKey(String key);
    @Transactional
    @Modifying
    @Query("update ChannelEntity set channelPhoto.id = :channelPhotoId where key = :key")
    void updateAttach(@Param("channelPhotoId") String attachId, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update ChannelEntity set lastModifiedDate=:lastModifiedDate where key=:key")
    void updateLastModifiedDate(@Param("lastModifiedDate") LocalDateTime lastModifiedDate, @Param("key") String key);
}