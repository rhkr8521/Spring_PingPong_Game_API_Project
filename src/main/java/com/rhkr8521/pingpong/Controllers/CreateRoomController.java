package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Services.CreateRoomService;
import com.rhkr8521.pingpong.dto.APIResponse;
import com.rhkr8521.pingpong.dto.CreateRoomRequest;
import com.rhkr8521.pingpong.Exceptions.API_201_Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="방 생성 API", description = "게임 방을 생성하는 API입니다.")
@RestController
public class CreateRoomController {

    private final CreateRoomService createRoomService;

    @Autowired
    public CreateRoomController(CreateRoomService createRoomService) {
        this.createRoomService = createRoomService;
    }

    // 방 생성
    @PostMapping("/room")
    public ResponseEntity<APIResponse> createRoom(@RequestBody CreateRoomRequest request) {
        try {
            createRoomService.createRoom(request);
            return ResponseEntity.ok(APIResponse.success(null));
        } catch (API_201_Exception ex) {
            return ResponseEntity.ok(APIResponse.badRequest());
        } catch (Exception ex) {
            return ResponseEntity.ok(APIResponse.serverError());
        }
    }
}
