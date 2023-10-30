package com.hyl.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * 密码
     */
    private String userPassword;

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
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否被删除
     */
    @TableLogic
    private Integer isDelete;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}