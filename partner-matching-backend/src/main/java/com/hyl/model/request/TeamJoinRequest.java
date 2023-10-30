package com.hyl.model.request;


import lombok.Data;

import java.io.Serializable;

@Data
public class TeamJoinRequest implements Serializable {
    private static final long serialVersionUID = -7962415320005027747L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍密码
     */
    private String password;


}
