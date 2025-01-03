package com.hyl.model.vo;



import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户包装类（脱敏）
 */

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -4663677866841230306L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0表示正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;


    /**
     * 用户角色 0普通用户 1管理员
     */
    private Integer userRole;

    /**
     * 校验账号
     */
    private String verifyCode;

    /**
     * 标签列表 json
     */
    private String tags;

    /**
     * 介绍
     */
    private String profile;


}
