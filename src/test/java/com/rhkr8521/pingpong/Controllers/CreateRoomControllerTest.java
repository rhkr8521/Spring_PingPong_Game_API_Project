package com.rhkr8521.pingpong.Controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.rhkr8521.pingpong.Services.CreateRoomService;
import com.rhkr8521.pingpong.dto.CreateRoomRequest;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

@ExtendWith(MockitoExtension.class)
public class CreateRoomControllerTest {

    @Mock
    private CreateRoomService createRoomService;

    @InjectMocks
    private CreateRoomController createRoomController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(createRoomController).build();
    }

    @Test
    void testCreateRoomSuccess() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest();

        mockMvc.perform(post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }

    @Test
    void testCreateRoomBadRequest() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest();
        doThrow(new API_201_Exception(null)).when(createRoomService).createRoom(any(CreateRoomRequest.class));

        mockMvc.perform(post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("불가능한 요청입니다."));
    }

    @Test
    void testCreateRoomServerError() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest();
        doThrow(new RuntimeException()).when(createRoomService).createRoom(any(CreateRoomRequest.class));

        mockMvc.perform(post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }
}
