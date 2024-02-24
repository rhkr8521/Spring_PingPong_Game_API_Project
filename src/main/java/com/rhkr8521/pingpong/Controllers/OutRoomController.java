package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Exceptions.API_201_Exception;
import com.rhkr8521.pingpong.Services.OutRoomService;
import com.rhkr8521.pingpong.dto.APIResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="방 나가기 API", description = "참가하고 있는 게임방을 나가는 API입니다.")
@RestController
public class OutRoomController {

    private final OutRoomService outRoomService;

    @Autowired
    public OutRoomController(OutRoomService outRoomService) {
        this.outRoomService = outRoomService;
    }

    // 방 나가기
    @PostMapping("/room/out/{roomId}")
    public ResponseEntity<APIResponse> outRoom(@PathVariable Integer roomId, @RequestBody Map<String, Integer> requestBody) {
        Integer userId = requestBody.get("userId");
        try {
            outRoomService.leaveRoom(roomId, userId);
            return ResponseEntity.ok(APIResponse.success(null));
        } catch (API_201_Exception ex) {
            return ResponseEntity.ok(APIResponse.badRequest());
        } catch (Exception ex) {
            return ResponseEntity.ok(APIResponse.serverError());
        }
    }
}
