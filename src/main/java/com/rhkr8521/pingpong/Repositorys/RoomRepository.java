package com.rhkr8521.pingpong.Repositorys;

import com.rhkr8521.pingpong.Entitys.RoomEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
    Optional<RoomEntity> findById(Integer id);
}
