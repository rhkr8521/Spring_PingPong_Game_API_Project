package com.rhkr8521.pingpong.Repositorys;

import com.rhkr8521.pingpong.Entitys.UserRoomEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoomEntity, Integer> {
    boolean existsByUserId(Integer userId);
    long countByRoomId(Integer roomId);
    void deleteAllByRoomId(Integer roomId);
    long countByRoomIdAndTeam(Integer roomId, UserRoomEntity.Team team);
    Optional<UserRoomEntity> findByRoomIdAndUserId(Integer roomId, Integer userId);

    @Transactional
    void deleteByRoomId(Integer roomId);
}