package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity.Team;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeTeamService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    @Autowired
    public ChangeTeamService(RoomRepository roomRepository, UserRoomRepository userRoomRepository) {
        this.roomRepository = roomRepository;
        this.userRoomRepository = userRoomRepository;
    }

    @Transactional
    public void changeTeam(Integer roomId, Integer userId) {

        // 방 ID가 존재하지 않을때 201 호출
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new API_201_Exception(null));

        // 유저 ID, 방 ID가 일치하지 않을때 201 호출
        UserRoomEntity userRoom = userRoomRepository.findByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new API_201_Exception(null));

        // 팀 조회 후 반대팀으로 변경
        Team currentTeam = userRoom.getTeam();
        Team oppositeTeam = (currentTeam == Team.RED) ? Team.BLUE : Team.RED;

        // 반대 팀원 수 와 절반 체크 후 일치하다면 201 호출
        long teamCount = userRoomRepository.countByRoomIdAndTeam(roomId, oppositeTeam);
        long halfCapacity = (room.getRoomType() == RoomEntity.RoomType.SINGLE) ? 1 : 2;

        if (teamCount >= halfCapacity) {
            throw new API_201_Exception(null);
        }

        userRoom.setTeam(oppositeTeam);
        userRoomRepository.save(userRoom);
    }
}
