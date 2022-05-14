package com.company.repository;

import com.company.dto.ChannelDTO;
import com.company.entity.ChannelEntity;
import com.company.enums.ChannelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Integer> {
    Optional<ChannelEntity> findByProfileIdAndKey(Integer pId, String key);

    Optional<ChannelEntity> findByKey(String key);

    @Transactional
    @Modifying
    @Query("update ChannelEntity set channelPhoto.id = :channelPhotoId where key = :key")
    void updateChannelPhoto(@Param("channelPhotoId") String attachId, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update ChannelEntity set bannerPhoto.id = :bannerPhotoId where key = :key")
    void updateBannerPhoto(@Param("bannerPhotoId") String attachId, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update ChannelEntity set lastModifiedDate=:lastModifiedDate where key=:key")
    void updateLastModifiedDate(@Param("lastModifiedDate") LocalDateTime lastModifiedDate, @Param("key") String key);

    @Transactional
    @Modifying
    @Query("update ChannelEntity set status=:status where key = :key")
    void updateChannelStatus(@Param("status") ChannelStatus status, @Param("key") String key);

    List<ChannelEntity> findByProfileId(Integer id);
}