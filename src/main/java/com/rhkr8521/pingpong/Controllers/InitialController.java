package com.rhkr8521.pingpong.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rhkr8521.pingpong.Services.InitialService;
import com.rhkr8521.pingpong.dto.APIResponse;
import com.rhkr8521.pingpong.dto.InitialRequest;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="초기화 API", description = "기존 정보들을 삭제하고 새로운 유저데이터를 넣는 API입니다.")
@RestController
public class InitialController {

    private final InitialService initialService;

    public InitialController(InitialService initialService) {
        this.initialService = initialService;
    }

    // 초기화 수행
    @PostMapping("/init")
    public ResponseEntity<APIResponse<String>> initializeDatabase(@RequestBody InitialRequest initRequest) {
        initialService.initialize(initRequest.getSeed(), initRequest.getQuantity());
        return ResponseEntity.ok(APIResponse.success(null));
    }
}
