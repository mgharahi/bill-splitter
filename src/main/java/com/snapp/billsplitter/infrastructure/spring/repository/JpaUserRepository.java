package com.snapp.billsplitter.infrastructure.spring.repository;

import com.snapp.billsplitter.infrastructure.spring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query(value = """
            select u.id 
            from users u
            inner join events_users eu on u.id = eu.users_id
            where eu.event_entity_id = :eventId""", nativeQuery = true)
    Set<Long> findByEventId(@Param("eventId") long eventId);
}