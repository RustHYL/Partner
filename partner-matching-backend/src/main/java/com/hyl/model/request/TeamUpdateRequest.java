package com.hyl.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamUpdateRequest implements Serializable {
    private static final long serialVersionUID = 7883453678359689035L;

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
     * 过期时间
     */
    private Date expireTime;

    /**
     * 状态 0-表示公开，1-表示私有，2-表示加密
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;


}
