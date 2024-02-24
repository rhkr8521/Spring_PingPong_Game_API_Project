package com.rhkr8521.pingpong.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllRoomResponse {
    private Integer id;
    private String title;
    private Integer hostId;
    private String roomType;
    private String status;
    
}