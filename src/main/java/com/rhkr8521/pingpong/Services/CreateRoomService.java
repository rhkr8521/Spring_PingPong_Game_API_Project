package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.dto.CreateRoomRequest;
import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateRoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Autowired
    public CreateRoomService(RoomRepository roomRepository, UserRepository userRepository, UserRoomRepository userRoomRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.userRoomRepository = userRoomRepository;
    }

    // 방 등록
    public void createRoom(CreateRoomRequest request) {
        // 해당 유저가 없을때 201 호출
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new API_201_Exception(null));

        // 해당 유저가 ACTIVE가 아닐때 201 호출
        if (user.getStatus() != UserEntity.UserStatus.ACTIVE) {
            throw new API_201_Exception(null);
        }

        // 해당 유저가 이미 다른 방에 존재할때 201 호출
        if (!isUserIdValidForRoomCreation(user)) {
            throw new API_201_Exception(null);
        }

        RoomEntity room = new RoomEntity();

        // 방 생성
        room.setHostId(request.getUserId());
        if ("SINGLE".equals(request.getRoomType())) {
            room.setRoomType(RoomEntity.RoomType.SINGLE);
        } else if ("DOUBLE".equals(request.getRoomType())) {
            room.setRoomType(RoomEntity.RoomType.DOUBLE);
        }
        room.setStatus(RoomEntity.RoomStatus.WAIT);
        room.setTitle(request.getTitle());
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        roomRepository.save(room);

        // UserRoom 생성 - Host -> RED팀
        UserRoomEntity userRoom = new UserRoomEntity();
        userRoom.setRoomId(room.getId());
        userRoom.setUserId(request.getUserId());
        userRoom.setTeam(UserRoomEntity.Team.RED);

        userRoomRepository.save(userRoom);
    }

    // 기존 유저 존재여부 확인
    private boolean isUserIdValidForRoomCreation(UserEntity user) {
        return !userRoomRepository.existsByUserId(user.getId());
    }
}
