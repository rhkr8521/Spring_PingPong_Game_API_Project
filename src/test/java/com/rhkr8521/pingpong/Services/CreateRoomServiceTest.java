package com.rhkr8521.pingpong.Services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.time.LocalDateTime;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Entitys.UserRoomEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;
import com.rhkr8521.pingpong.dto.CreateRoomRequest;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

@ExtendWith(MockitoExtension.class)
public class CreateRoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private CreateRoomService createRoomService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateRoomSuccess() {
        int userId = 1;
        CreateRoomRequest request = new CreateRoomRequest();
        request.setUserId(userId);
        request.setRoomType("SINGLE");
        request.setTitle("Test Room");

        UserEntity mockUser = new UserEntity();
        mockUser.setId(userId);
        mockUser.setStatus(UserEntity.UserStatus.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRoomRepository.existsByUserId(userId)).thenReturn(false);

        RoomEntity savedRoom = new RoomEntity();
        savedRoom.setHostId(userId);
        savedRoom.setRoomType(RoomEntity.RoomType.SINGLE);
        savedRoom.setTitle(request.getTitle());
        savedRoom.setStatus(RoomEntity.RoomStatus.WAIT);
        savedRoom.setCreatedAt(LocalDateTime.now());
        savedRoom.setUpdatedAt(LocalDateTime.now());

        when(roomRepository.save(any(RoomEntity.class))).thenReturn(savedRoom);

        UserRoomEntity savedUserRoom = new UserRoomEntity();
        savedUserRoom.setRoomId(savedRoom.getId());
        savedUserRoom.setUserId(userId);
        savedUserRoom.setTeam(UserRoomEntity.Team.RED);

        when(userRoomRepository.save(any(UserRoomEntity.class))).thenReturn(savedUserRoom);

        createRoomService.createRoom(request);

        verify(userRepository).findById(userId);
        verify(userRoomRepository).existsByUserId(userId);
        verify(roomRepository).save(any(RoomEntity.class));
        verify(userRoomRepository).save(any(UserRoomEntity.class));
    }

    @Test
    void testCreateRoomUserNotFound() {
        CreateRoomRequest request = new CreateRoomRequest();
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(API_201_Exception.class, () -> createRoomService.createRoom(request));
    }

    @Test
    void testCreateRoomUserNotActive() {
        CreateRoomRequest request = new CreateRoomRequest();

        UserEntity mockUser = new UserEntity();
        mockUser.setStatus(UserEntity.UserStatus.NON_ACTIVE); // User is not active
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(mockUser));

        assertThrows(API_201_Exception.class, () -> createRoomService.createRoom(request));
    }

    @Test
    void testCreateRoomUserAlreadyInRoom() {
        int userId = 1;
        CreateRoomRequest request = new CreateRoomRequest();
        request.setUserId(userId);
        request.setRoomType("SINGLE");
        request.setTitle("Test Room");

        UserEntity mockUser = new UserEntity();
        mockUser.setId(userId);
        mockUser.setStatus(UserEntity.UserStatus.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRoomRepository.existsByUserId(userId)).thenReturn(true);

        assertThrows(API_201_Exception.class, () -> createRoomService.createRoom(request));

        verify(userRepository).findById(userId);
        verify(userRoomRepository).existsByUserId(userId);

        verify(roomRepository, never()).save(any(RoomEntity.class));
        verify(userRoomRepository, never()).save(any(UserRoomEntity.class));
    }
}