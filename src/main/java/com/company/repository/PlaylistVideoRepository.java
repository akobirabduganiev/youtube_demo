package com.company.repository;

import com.company.entity.PlaylistVideoEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideoEntity, Integer> {
    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(Integer playlistId, Integer videoId);

    List<PlaylistVideoEntity> findAllByPlaylistId(Integer playlistId, Sort sort);

    Optional<PlaylistVideoEntity> findByVideoId(Integer videoId);

    @Query("select count(id) from PlaylistVideoEntity where playlistId = :playlistId")
    int getVideoCountByPlaylistId(@Param("playlistId") Integer playlistId);

    @Query(value = "select * from playlist_video where playlist_id = :playlistId order by order_num limit 2"
            , nativeQuery = true)
    List<PlaylistVideoEntity> getTop2VideoByPlaylistId(@Param("playlistId") Integer playlistId);
}