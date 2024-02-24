package com.rhkr8521.pingpong.Controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rhkr8521.pingpong.Services.ChangeTeamService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ChangeTeamControllerTest {

    @Mock
    private ChangeTeamService changeTeamService;

    @InjectMocks
    private ChangeTeamController changeTeamController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(changeTeamController).build();
    }

    @Test
    void changeTeamSuccess() throws Exception {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("userId", 1);

        mockMvc.perform(put("/team/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));

        verify(changeTeamService).changeTeam(1, 1);
    }

}
