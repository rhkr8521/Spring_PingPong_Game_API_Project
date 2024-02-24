package com.rhkr8521.pingpong.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.rhkr8521.pingpong.Services.FindAllElementService;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Entitys.RoomEntity;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FindAllElementControllerTest {

    @Mock
    private FindAllElementService findService;

    @InjectMocks
    private FindAllElementController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testFindAllUsers() throws Exception {
        Page<UserEntity> userPage = new PageImpl<>(Collections.singletonList(new UserEntity()));
        when(findService.findAllUsers(any(PageRequest.class))).thenReturn(userPage);

        mockMvc.perform(get("/user").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."))
                .andExpect(jsonPath("$.result.totalElements").value(1));
    }

    @Test
    public void testFindAllRooms() throws Exception {
        Page<AllRoomResponse> roomPage = new PageImpl<>(Collections.singletonList(new AllRoomResponse()));
        when(findService.findAllRooms(any(PageRequest.class))).thenReturn(roomPage);

        mockMvc.perform(get("/room").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."))
                .andExpect(jsonPath("$.result.totalElements").value(1));
    }

    @Test
    public void testFindRoomById() throws Exception {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(1);
        roomEntity.setTitle("Test Room");
        roomEntity.setHostId(1);
        roomEntity.setRoomType(RoomEntity.RoomType.SINGLE);
        roomEntity.setStatus(RoomEntity.RoomStatus.WAIT);

        when(findService.findRoomById(1)).thenReturn(Optional.of(roomEntity));

        mockMvc.perform(get("/room/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."))
                .andExpect(jsonPath("$.result.id").value(1));
    }

}
