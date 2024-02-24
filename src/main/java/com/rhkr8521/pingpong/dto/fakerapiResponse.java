package com.rhkr8521.pingpong.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class fakerapiResponse {

    @Getter
    @Setter
    private List<FakerUser> data;

    @Getter
    @Setter
    public static class FakerUser {
        private Integer id;
        private String email;
        private String username;

    }
}
