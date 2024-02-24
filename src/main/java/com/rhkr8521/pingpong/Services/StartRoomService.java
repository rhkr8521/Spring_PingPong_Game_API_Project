package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.RoomEntity.RoomStatus;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PreDestroy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StartRoomService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final ScheduledExecutorService scheduler;

    @Autowired
    public StartRoomService(RoomRepository roomRepository, UserRoomRepository userRoomRepository) {
        this.roomRepository = roomRepository;
        this.userRoomRepository = userRoomRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Transactional
    public void startRoom(Integer roomId, Integer userId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new API_201_Exception(null));

        // 전달된 User ID가 해당 방의 Host ID와 다를때 201 호출
        if (!room.getHostId().equals(userId)) {
            throw new API_201_Exception(null);
        }

        // 해당 방의 인원이 꽉 차지 않았을때 201 호출  
        long userCount = userRoomRepository.countByRoomId(roomId);
        if ((room.getRoomType() == RoomEntity.RoomType.SINGLE && userCount != 2) ||
            (room.getRoomType() == RoomEntity.RoomType.DOUBLE && userCount != 4)) {
            throw new API_201_Exception(null);
        }

        // 해당 방 상태가 WAIT가 아닐때 201 호출
        if (room.getStatus() != RoomStatus.WAIT) {
            throw new API_201_Exception(null);
        }

        // API 호출 후 PROGRESS 변경
        room.setStatusWithTimestamp(RoomStatus.PROGRESS);
        roomRepository.save(room);

        // 60초 체크
        scheduler.schedule(() -> finishRoom(roomId), 60, TimeUnit.SECONDS);
    }

    // 60초 후 FINISH 변경
    private void finishRoom(Integer roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new API_201_Exception(null));

        room.setStatusWithTimestamp(RoomStatus.FINISH);
        roomRepository.save(room);
        deleteByRoomTables(roomId);
    }

    @PreDestroy
    public void preDestroy() {
        if (!scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    // FINISH로 변경후 User Rooms 데이터 삭제
    private void deleteByRoomTables(Integer roomId) {
        userRoomRepository.deleteByRoomId(roomId);
    }
}
