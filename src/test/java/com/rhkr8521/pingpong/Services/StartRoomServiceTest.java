package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

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
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StartRoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private StartRoomService startRoomService;

    @BeforeEach
    void setUp() {
        RoomEntity room = new RoomEntity();
        room.setId(1);
        room.setHostId(1);
        room.setRoomType(RoomEntity.RoomType.SINGLE);
        room.setStatus(RoomEntity.RoomStatus.WAIT);
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
    }

    @Test
    void testStartRoomSuccess() {
        when(userRoomRepository.countByRoomId(anyInt())).thenReturn(2L);

        startRoomService.startRoom(1, 1);

        verify(roomRepository).save(any(RoomEntity.class));
    }

    @Test
    void testStartRoomFailure_NotHost() {
        assertThrows(API_201_Exception.class, () -> startRoomService.startRoom(1, 2));
    }

}
