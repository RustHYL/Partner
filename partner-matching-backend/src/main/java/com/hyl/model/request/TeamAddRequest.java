package com.hyl.model.request;

import java.io.Serializable;
import java.util.Date;

public class TeamAddRequest implements Serializable {

    private static final long serialVersionUID = 8739599342205365566L;
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

}
