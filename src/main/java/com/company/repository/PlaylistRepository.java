package com.company.repository;

import com.company.entity.PlaylistEntity;
import com.company.enums.PlaylistStatus;
import com.company.mapper.PlayListInfoAdminMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Integer> {

    Optional<PlaylistEntity> findByChannelIdAndName(Integer channelId, String name);

    @Transactional
    @Modifying
    @Query("update PlaylistEntity set status = :status where id = :id")
    int updateStatus(@Param("status") PlaylistStatus status, @Param("id") Integer id);

    List<PlaylistEntity> findAllByChannelIdAndStatus(Integer id, PlaylistStatus status, Sort sort);

    @Transactional
    @Modifying
    @Query("update PlaylistEntity set name=:name, description=:description where id=:id")
    void updatePlaylistDetail(@Param("name") String name, @Param("description") String description, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update PlaylistEntity set lastModifiedDate=:date where id=:id")
    void updateLastModifiedDate(@Param("date") LocalDateTime lastModifiedDate, @Param("id") Integer id);

    @Query("from PlaylistEntity where channel.profileId = :profileId")
    List<PlaylistEntity> findAllByProfileId(@Param("profileId") Integer profileId, Sort sort);

    @Transactional
    @Modifying
    @Query("update PlaylistEntity set status=:status where id=:id")
    void changeStatus(@Param("status") PlaylistStatus status, @Param("id") Integer id);

    @Query(value = "select  cast(pl.id as varchar) as pl_id,pl.name as pl_name,pl.description as pl_description,pl.status as pl_status,pl.order_number, " +
            "cast(ch.id as varchar) as ch_id, ch.name as ch_name,cast(ch.id as varchar) as ch_photo_id, " +
            "cast(pr.id as varchar) as pr_id,pr.name as pr_name,pr.surname as pr_surname, cast(pr.profile_photo_id as varchar) as pr_photo_id " +
            "from playlist as pl " +
            "inner join channel as ch on pl.channel_id = ch.id " +
            "inner join profile as pr on pr.id = ch.profile_id " +
            "order by pl.created_date", nativeQuery = true)
     Page<PlayListInfoAdminMapper> getPlaylistInfoForAdmin(Pageable pageable);
/*
    @Query("SELECT pl.id as pl_id,pl.name as pl_name,pl.description as pl_description,pl.status as pl_status,pl.orderNumber as pl_order_num, " +
            "ch.id as ch_id, ch.name as ch_name, ch.id as ch_photo_id, " +
            "pr.id as pr_id, pr.name as pr_name,pr.surname as pr_surname,pr.attachId as pr_photo_id " +
            "FROM PlaylistEntity  as pl " +
            " INNER JOIN pl.channel as ch " +
            " INNER JOIN ch.profile as pr " +
            " order by pl.createdDate desc ")
    Page<PlayListInfoJpqlAdminMapper> getPlaylistInfoJpql(Pageable pageable);*/
}