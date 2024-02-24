package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JoinRoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Autowired
    public JoinRoomService(RoomRepository roomRepository, UserRepository userRepository, UserRoomRepository userRoomRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.userRoomRepository = userRoomRepository;
    }

    public boolean joinRoom(Integer roomId, Integer userId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new API_201_Exception(null));

        // 해당 방이 WAIT가 아닐때 201 호출
        if (room.getStatus() != RoomEntity.RoomStatus.WAIT) {
            throw new API_201_Exception(null);
        }

        // 해당 유저 ID가 적절하지 않을때 201 호출
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new API_201_Exception(null));

        // 해당 유저가 ACTIVE상태가 아닐때 201 호출
        if (user.getStatus() != UserEntity.UserStatus.ACTIVE) {
            throw new API_201_Exception(null);
        }

        // 이미 다른 방에 참가한상태 일시 201 호출
        if (userRoomRepository.existsByUserId(userId)) {
            throw new API_201_Exception(null);
        }

        // 방 정원 초과 시 201 호출
        long count = userRoomRepository.countByRoomId(roomId);
        if ((room.getRoomType() == RoomEntity.RoomType.SINGLE && count >= 2) ||
            (room.getRoomType() == RoomEntity.RoomType.DOUBLE && count >= 4)) {
                throw new API_201_Exception(null);
        }

        UserRoomEntity userRoom = new UserRoomEntity();
        userRoom.setRoomId(roomId);
        userRoom.setUserId(userId);

        // 팀 분배
        if (room.getRoomType() == RoomEntity.RoomType.DOUBLE) {
            long redCount = userRoomRepository.countByRoomIdAndTeam(roomId, UserRoomEntity.Team.RED);
            userRoom.setTeam(redCount == 1 ? UserRoomEntity.Team.RED : UserRoomEntity.Team.BLUE);
        } else if (room.getRoomType() == RoomEntity.RoomType.SINGLE || (room.getRoomType() == RoomEntity.RoomType.DOUBLE && count >= 2)) {
            userRoom.setTeam(UserRoomEntity.Team.BLUE);
        } else {
            userRoom.setTeam(UserRoomEntity.Team.RED);
        }

        userRoomRepository.save(userRoom);

        return true;
    }
}