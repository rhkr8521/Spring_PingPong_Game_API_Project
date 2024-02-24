package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Services.FindAllElementService;
import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.dto.APIResponse;
import com.rhkr8521.pingpong.dto.AllRoomResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="데이터 조회 API", description = "전체 사용자, 전체 방, 상세 방 조회 API입니다.")
@RestController
public class FindAllElementController {

    private final FindAllElementService findService;

    @Autowired
    public FindAllElementController(FindAllElementService findService) {
        this.findService = findService;
    }

    // 모든 사용자 조회
    @Operation(summary = "전체 사용자 조회", description = "등록되어있는 사용자 전체를 조회합니다.")
    @GetMapping("/user")
    public ResponseEntity<APIResponse> findAllUsers(
            @RequestParam int page,
            @RequestParam int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UserEntity> userPage = findService.findAllUsers(pageRequest);

        Map<String, Object> result = new LinkedHashMap<>(); // Use LinkedHashMap
        result.put("totalElements", userPage.getTotalElements());
        result.put("totalPages", userPage.getTotalPages());
        result.put("userList", userPage.getContent());

        return ResponseEntity.ok(APIResponse.success(result));
    }

    // 모든 방 조회
    @Operation(summary = "전체 방 조회", description = "등록되어있는 방 전체를 조회합니다.")
    @GetMapping("/room")
    public ResponseEntity<APIResponse> findAllRooms(
            @RequestParam int page,
            @RequestParam int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AllRoomResponse> roomPage = findService.findAllRooms(pageRequest);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalElements", roomPage.getTotalElements());
        result.put("totalPages", roomPage.getTotalPages());
        result.put("roomList", roomPage.getContent());

        return ResponseEntity.ok(APIResponse.success(result));
    }

    // 방ID 상세 조회
    @Operation(summary = "방 상세 조회", description = "등록되어있는 방을 상세 조회합니다.")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<APIResponse<Map<String, Object>>> findRoomById(@PathVariable Integer roomId) {
        return findService.findRoomById(roomId)
            .map(room -> {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("id", room.getId());
                result.put("title", room.getTitle());
                result.put("hostId", room.getHostId());
                result.put("roomType", room.getRoomType().toString());
                result.put("status", room.getStatus().toString());
                result.put("createdAt", room.getCreatedAt() != null ? room.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
                result.put("updatedAt", room.getUpdatedAt() != null ? room.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
                return ResponseEntity.ok(APIResponse.success(result));
            })
            .orElseGet(() -> ResponseEntity.ok(APIResponse.badRequest()));
    }
}