package com.hyl.common;

import lombok.Data;

import java.io.Serializable;


/**
 * 通用删除请求
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = -3529254662515644528L;

    private long id;
}
