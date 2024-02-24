package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Services.StartRoomService;
import com.rhkr8521.pingpong.dto.APIResponse;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="게임 시작 API", description = "호스트로 등록되어있는 게임을 시작하는 API입니다.")
@RestController
public class StartRoomController {

    private final StartRoomService startRoomService;

    @Autowired
    public StartRoomController(StartRoomService startRoomService) {
        this.startRoomService = startRoomService;
    }

    // 방 시작
    @PutMapping("/room/start/{roomId}")
    public ResponseEntity<APIResponse> startRoom(@PathVariable Integer roomId, @RequestBody Map<String, Integer> requestBody) {
        try {
            Integer userId = requestBody.get("userId");
            startRoomService.startRoom(roomId, userId);
            return ResponseEntity.ok(APIResponse.success(null));
        } catch (API_201_Exception e) {
            return ResponseEntity.ok(APIResponse.badRequest());
        } catch (Exception ex) {
            return ResponseEntity.ok(APIResponse.serverError());
        }
    }
}
