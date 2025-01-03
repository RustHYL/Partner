package com.hyl.common;

import lombok.Data;

/**
 * @author Huang YuLong
 * @description TODO
 * @date 2023-09-18 9:23
 * @Param 全局错误码
 */
public enum ErrorCode {
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    PARAMS_NULL_ERROR(40001, "请求数据为空", ""),
    NO_AUTH(40100,"没有权限",""),
    NO_LOGIN(40101, "未登录", ""),
    FORBIDDEN(40301, "禁止访问", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.description = description;
        this.message = message;
    }

    public int getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
