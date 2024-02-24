package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.dto.AllRoomResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FindAllElementService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public FindAllElementService(UserRepository userRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    // 모든 사용자 조회
    public Page<UserEntity> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // 모든 방 조회
    public Page<AllRoomResponse> findAllRooms(Pageable pageable) {
        Page<RoomEntity> roomPage = roomRepository.findAll(pageable);
        return roomPage.map(this::convertToRoomDTO);
    }

    private AllRoomResponse convertToRoomDTO(RoomEntity roomEntity) {
        AllRoomResponse roomDTO = new AllRoomResponse();
        roomDTO.setId(roomEntity.getId());
        roomDTO.setTitle(roomEntity.getTitle());
        roomDTO.setHostId(roomEntity.getHostId());
        roomDTO.setRoomType(roomEntity.getRoomType().toString());
        roomDTO.setStatus(roomEntity.getStatus().toString());
        return roomDTO;
    }

    // 방ID 상세조회
    public Optional<RoomEntity> findRoomById(Integer roomId) {
        return roomRepository.findById(roomId);
    }
}