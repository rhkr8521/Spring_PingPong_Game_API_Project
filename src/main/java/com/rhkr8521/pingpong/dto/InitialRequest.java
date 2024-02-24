package com.rhkr8521.pingpong.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InitialRequest {
    private int seed;
    private int quantity;

}
