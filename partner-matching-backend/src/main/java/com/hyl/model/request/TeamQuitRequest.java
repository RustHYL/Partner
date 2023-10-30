package com.hyl.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍
 */
@Data
public class TeamQuitRequest implements Serializable {


    private static final long serialVersionUID = -8385906217212515583L;

    /**
     * id
     */
    private Long id;

}
