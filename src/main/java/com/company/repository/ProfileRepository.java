package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status = :status where id = :id")
    void updateStatus(@Param("status") ProfileStatus status, @Param("id") Integer id);

    Optional<ProfileEntity> findByEmailAndPassword(String email, String password);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set attach.id = :attachId where id = :id")
    void updateAttach(@Param("attachId") String attachId, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set lastModifiedDate=:lastModifiedDate where id=:id")
    void updateLastModifiedDate(@Param("lastModifiedDate") LocalDateTime lastModifiedDate, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set name=:name, surname=:surname where id=:id")
    void updateProfileDetail(@Param("name") String name, @Param("surname") String surname, @Param("id") Integer id);

}