package com.rhkr8521.pingpong.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    private Integer code;
    private String message;
    private T result;

    public APIResponse(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    // 200 응답
    public static <T> APIResponse<T> success(T result) {
        return new APIResponse<>(200, "API 요청이 성공했습니다.", result);
    }

    // 201 응답
    public static <T> APIResponse<T> badRequest() {
        return new APIResponse<>(201, "불가능한 요청입니다.", null);
    }

    // 500 응답
    public static <T> APIResponse<T> serverError() {
        return new APIResponse<>(500, "에러가 발생했습니다.", null);
    }
}
