package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class JoinRoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private JoinRoomService joinRoomService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testJoinRoomSuccess() {
        Integer roomId = 1;
        Integer userId = 100;

        RoomEntity mockRoom = new RoomEntity();
        mockRoom.setStatus(RoomEntity.RoomStatus.WAIT);
        mockRoom.setRoomType(RoomEntity.RoomType.SINGLE);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));

        UserEntity mockUser = new UserEntity();
        mockUser.setStatus(UserEntity.UserStatus.ACTIVE);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        when(userRoomRepository.existsByUserId(userId)).thenReturn(false);
        when(userRoomRepository.countByRoomId(roomId)).thenReturn(0L);

        assertTrue(joinRoomService.joinRoom(roomId, userId));

        verify(roomRepository).findById(roomId);
        verify(userRepository).findById(userId);
        verify(userRoomRepository).existsByUserId(userId);
        verify(userRoomRepository).countByRoomId(roomId);
        verify(userRoomRepository).save(any());
    }

    @Test
    void testJoinRoomFailure() {
        Integer roomId = 1;
        Integer userId = 100;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(API_201_Exception.class, () -> joinRoomService.joinRoom(roomId, userId));

        verify(roomRepository).findById(roomId);
        verify(userRepository, never()).findById(any());
    }
}
