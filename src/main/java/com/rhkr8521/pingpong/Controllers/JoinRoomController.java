package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Services.JoinRoomService;
import com.rhkr8521.pingpong.dto.APIResponse;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="방 참가 API", description = "게임 방에 참가하는 API입니다.")
@RestController
public class JoinRoomController {

    private final JoinRoomService joinRoomService;

    @Autowired
    public JoinRoomController(JoinRoomService joinRoomService) {
        this.joinRoomService = joinRoomService;
    }

    // 방 참가
    @PostMapping("/room/attention/{roomId}")
    public ResponseEntity<APIResponse> joinRoom(@PathVariable Integer roomId, @RequestBody Map<String, Integer> requestBody) {
        Integer userId = requestBody.get("userId");
        try {
            joinRoomService.joinRoom(roomId, userId);
            return ResponseEntity.ok(APIResponse.success(null));
        } catch (API_201_Exception ex) {
            return ResponseEntity.ok(APIResponse.badRequest());
        } catch (Exception ex) {
            return ResponseEntity.ok(APIResponse.serverError());
        }
    }
}