package com.rhkr8521.pingpong.Controllers;

import com.rhkr8521.pingpong.Exceptions.API_201_Exception;
import com.rhkr8521.pingpong.Services.ChangeTeamService;
import com.rhkr8521.pingpong.dto.APIResponse;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="팀 변경 API", description = "반대 팀으로 변경하는 API입니다.")
@RestController
public class ChangeTeamController {
    
    private final ChangeTeamService changeTeamService;

    @Autowired
    public ChangeTeamController(ChangeTeamService changeTeamService) {
        this.changeTeamService = changeTeamService;
    }

    // 팀 변경
    @PutMapping("/team/{roomId}")
    public ResponseEntity<APIResponse> changeTeam(@PathVariable Integer roomId, @RequestBody Map<String, Integer> requestBody) {
        Integer userId = requestBody.get("userId");
        try {
            changeTeamService.changeTeam(roomId, userId);
            return ResponseEntity.ok(APIResponse.success(null));
        } catch (API_201_Exception ex) {
            return ResponseEntity.ok(APIResponse.badRequest());
        } catch (Exception ex) {
            return ResponseEntity.ok(APIResponse.serverError());
        }
    }
}
