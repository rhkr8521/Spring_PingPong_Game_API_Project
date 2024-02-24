package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;
import com.rhkr8521.pingpong.dto.fakerapiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
public class InitialServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoomRepository roomRepository;
    
    @Mock
    private UserRoomRepository userRoomRepository;
    
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InitialService initialService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testInitialize() {
        int seed = 1;
        int quantity = 10;
        fakerapiResponse.FakerUser fakeUser = new fakerapiResponse.FakerUser();
        fakeUser.setId(1);
        fakeUser.setEmail("test@example.com");
        fakeUser.setUsername("testuser");
        
        List<fakerapiResponse.FakerUser> fakeUsers = new ArrayList<>();
        fakeUsers.add(fakeUser);
        
        fakerapiResponse mockResponse = new fakerapiResponse();
        mockResponse.setData(fakeUsers);
        when(restTemplate.getForObject(anyString(), eq(fakerapiResponse.class))).thenReturn(mockResponse);

        initialService.initialize(seed, quantity);

        verify(userRepository, times(1)).deleteAll();
        verify(roomRepository, times(1)).deleteAll();
        verify(userRoomRepository, times(1)).deleteAll();
        verify(restTemplate, times(1)).getForObject(anyString(), eq(fakerapiResponse.class));
        verify(userRepository, atLeastOnce()).save(any(UserEntity.class));
    }
}
