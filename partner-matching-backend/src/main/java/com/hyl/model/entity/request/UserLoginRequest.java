package com.hyl.model.entity.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huang YuLong
 * @description TODO
 * @date 2023-09-08 15:33
 */

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -8916917342963830493L;

    private String userAccount;

    private String userPassword;
}
