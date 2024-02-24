package com.rhkr8521.pingpong.Controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhkr8521.pingpong.dto.APIResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="헬스체크 API", description = "서버의 상태를 체크하는 API입니다.")
@RestController
public class HealthController {

    // API 정상 테스트
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<Object>> checkHealth() {
        return ResponseEntity.ok(APIResponse.success(null));
    }
}



