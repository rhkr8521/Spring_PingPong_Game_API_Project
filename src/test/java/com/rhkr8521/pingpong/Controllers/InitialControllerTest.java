package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Services.InitialService;
import com.rhkr8521.pingpong.dto.APIResponse;
import com.rhkr8521.pingpong.dto.InitialRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InitialControllerTest {

    @Mock
    private InitialService initialService;

    @InjectMocks
    private InitialController initialController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testInitializeDatabase() {
        InitialRequest request = new InitialRequest();
        request.setSeed(123);
        request.setQuantity(10);

        ResponseEntity<APIResponse<String>> response = initialController.initializeDatabase(request);

        verify(initialService, times(1)).initialize(request.getSeed(), request.getQuantity());
        assertEquals(200, response.getStatusCode().value());
        assertEquals("API 요청이 성공했습니다.", response.getBody().getMessage());
    }
}
