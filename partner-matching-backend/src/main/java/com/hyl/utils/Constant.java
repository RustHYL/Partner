package com.hyl.utils;

/**
 * @author Huang YuLong
 * @description TODO
 * @date 2023-09-07 17:49
 */
public interface Constant {



    /**
     * 盐值 混淆密码
     */
    public static final String SALT = "maowulang";

    /**
     *用户登录态键
     */
    public static final String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户角色管理员
     */
    public static final Integer ADMIN_ROLE = 1;

    /**
     * 用户角色普通用户
     */
    public static final Integer DEFAULT_ROLE = 0;

    /**
     * Redis推荐key前缀
     */
    public static final String REDIS_RECOMMEND_PREFIX = "partner:recommend:";

    /**
     * Redis推荐过期时间
     */
    public static final Integer REDIS_RECOMMEND_TIME = 60;

    /**
     * Redis推荐key前缀
     */
    public static final String REDIS_RRECACHEJOB_PREFIX = "partner:precachejob:";

    /**
     * 单人队伍限制个数
     */
    public static final long PERSONAL_TEAM_MAX_COUNT = 5L;

}
