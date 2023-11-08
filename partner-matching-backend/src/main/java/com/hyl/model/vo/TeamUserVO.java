package com.hyl.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 队伍和用户信息封装类（脱敏）
 */
@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = 6501231939143991633L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 0-表示公开，1-表示私有，2-表示加密
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;


    /**
     * 队伍的创建人信息
     */
    UserVO createUser;

    /**
     * 是否已加入该队伍
     */
    private boolean hasJoin = false;
}
