package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.RoomEntity;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.dto.AllRoomResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FindAllElementServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private FindAllElementService findAllElementService;

    private PageRequest pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testFindAllUsers() {
        Page<UserEntity> expectedPage = new PageImpl<>(Collections.singletonList(new UserEntity()));
        when(userRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<UserEntity> result = findAllElementService.findAllUsers(pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void testFindAllRooms() {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomType(RoomEntity.RoomType.SINGLE);
        roomEntity.setStatus(RoomEntity.RoomStatus.WAIT);


        Page<RoomEntity> roomEntities = new PageImpl<>(Collections.singletonList(roomEntity));
        when(roomRepository.findAll(pageable)).thenReturn(roomEntities);

        Page<AllRoomResponse> result = findAllElementService.findAllRooms(pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(roomRepository).findAll(pageable);
    }


    @Test
    void testFindRoomById() {
        Integer roomId = 1;
        Optional<RoomEntity> expectedRoom = Optional.of(new RoomEntity());
        when(roomRepository.findById(roomId)).thenReturn(expectedRoom);

        Optional<RoomEntity> result = findAllElementService.findRoomById(roomId);

        assertTrue(result.isPresent());
        assertEquals(expectedRoom, result);
        verify(roomRepository).findById(roomId);
    }
}
