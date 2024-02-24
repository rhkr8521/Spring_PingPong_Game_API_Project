package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class OutRoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private OutRoomService outRoomService;

    @BeforeEach
    void setUp() {
        RoomEntity room = new RoomEntity();
        room.setHostId(1);
        room.setStatus(RoomEntity.RoomStatus.WAIT);

        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));

        UserEntity user = new UserEntity();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        when(userRoomRepository.findByRoomIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.of(new UserRoomEntity()));
    }

    @Test
    void testLeaveRoom() {
        Integer roomId = 1;
        Integer userId = 2;

        assertDoesNotThrow(() -> outRoomService.leaveRoom(roomId, userId));

        verify(userRoomRepository).delete(any(UserRoomEntity.class));
    }

}
