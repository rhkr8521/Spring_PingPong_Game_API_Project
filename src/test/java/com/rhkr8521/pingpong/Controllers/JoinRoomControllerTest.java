package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Services.JoinRoomService;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class JoinRoomControllerTest {

    @Mock
    private JoinRoomService joinRoomService;

    @InjectMocks
    private JoinRoomController joinRoomController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(joinRoomController).build();
    }

    @Test
    public void testJoinRoomSuccess() throws Exception {
        Integer roomId = 1;
        Integer userId = 100;
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("userId", userId);

        mockMvc.perform(post("/room/attention/" + roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));

        verify(joinRoomService, times(1)).joinRoom(roomId, userId);
    }

    @Test
    public void testJoinRoomBadRequest() throws Exception {
        Integer roomId = 1;
        Integer userId = 100;
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("userId", userId);

        doThrow(new API_201_Exception(null)).when(joinRoomService).joinRoom(roomId, userId);

        mockMvc.perform(post("/room/attention/" + roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("불가능한 요청입니다."));

        verify(joinRoomService, times(1)).joinRoom(roomId, userId);
    }
}
