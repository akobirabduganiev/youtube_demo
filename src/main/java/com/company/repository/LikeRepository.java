package com.company.repository;

import com.company.entity.LikeEntity;
import com.company.enums.LikeStatus;
import com.company.mapper.VideoLikeDislikeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    Page<LikeEntity> findAllByVideoId(Integer videoId, Pageable pageable);

    Page<LikeEntity> findAllByProfileId(Integer profile, Pageable pageable);

    Optional<LikeEntity> findByVideoIdAndProfileId(Integer videoId, Integer profileId);

    long countByVideoIdAndStatus(Integer articleId, LikeStatus status);

    @Query(value = "select sum( case  when status = 'LIKE' THEN 1 else 0 END ) like_count, " +
            " sum( case  when status = 'LIKE' THEN 0 else 1 END ) dislike_count " +
            "from like_table where article_id = ?1", nativeQuery = true)
    VideoLikeDislikeMapper countArticleLikeAndDislike(Integer videoId);
}
