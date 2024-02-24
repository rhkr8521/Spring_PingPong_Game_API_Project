package com.rhkr8521.pingpong.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequest {
    private int userId;
    private String roomType;
    private String title;
}
