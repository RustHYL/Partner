package com.hyl.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huang YuLong
 * @description TODO
 * @date 2023-09-18 8:47
 */

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 5251326283539594731L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse() {
    }

    public BaseResponse(int code, String message, String description) {
        this(code, null, message, description);
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode e){
        this(e.getCode(),null, e.getMessage(), e.getDescription());
    }

}
