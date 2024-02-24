package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ChangeTeamServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private ChangeTeamService changeTeamService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void changeTeamSuccess() {
        RoomEntity roomEntity = new RoomEntity();
        UserRoomEntity userRoomEntity = new UserRoomEntity();

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(roomEntity));
        when(userRoomRepository.findByRoomIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.of(userRoomEntity));
        when(userRoomRepository.countByRoomIdAndTeam(anyInt(), any())).thenReturn(0L);

        changeTeamService.changeTeam(1, 1);

        verify(userRoomRepository).save(any(UserRoomEntity.class));
    }

}
