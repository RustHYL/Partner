package com.hyl.model.request;

/**
 * @author Huang YuLong
 * @description TODO
 * @date 2023-09-08 14:44
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 6669919186371348280L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;

    /**
     * 检验账号
     */
    private String verifyCode;

}
