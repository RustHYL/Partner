package com.hyl.model.dto;



import com.hyl.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 队伍查询请求封装类
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest {
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
     * 用户id
     */
    private Long userId;

    /**
     * 状态 0-表示公开，1-表示私有，2-表示加密
     */
    private Integer status;

    /**
     * 搜索关键词（对队伍名称和介绍搜索）
     */
    private String searchText;

}
