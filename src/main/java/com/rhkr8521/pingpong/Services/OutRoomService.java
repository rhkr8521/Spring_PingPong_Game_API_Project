package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity;
import com.rhkr8521.pingpong.Entitys.RoomEntity.RoomStatus;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutRoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Autowired
    public OutRoomService(RoomRepository roomRepository, UserRepository userRepository, UserRoomRepository userRoomRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.userRoomRepository = userRoomRepository;
    }

    @Transactional
    public void leaveRoom(Integer roomId, Integer userId) {
        RoomEntity room = roomRepository.findById(roomId).orElse(null);

        // 방이 없거나, PROGRESS, FINISH 상태일때 201 호출
        if (room == null || room.getStatus() == RoomEntity.RoomStatus.PROGRESS || room.getStatus() == RoomEntity.RoomStatus.FINISH) {
            throw new API_201_Exception(null);
        }

        // User가 없을때 201 호출
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new API_201_Exception(null);
        }

        // User ID가 Host ID 일경우 방 폐쇄(FINISH), 해당 유저 Out
        if (room.getHostId().equals(userId)) {
            userRoomRepository.deleteAllByRoomId(roomId);
            room.setStatusWithTimestamp(RoomStatus.FINISH);
            roomRepository.save(room);
        } else {
            UserRoomEntity userRoom = userRoomRepository.findByRoomIdAndUserId(roomId, userId).orElse(null);
            if (userRoom != null) {
                userRoomRepository.delete(userRoom);
            } else {
                throw new API_201_Exception(null);
            }
        }
    }
}
