package com.hyl.service;

import com.hyl.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.model.entity.User;

/**
* @author Alan
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2023-10-26 13:40:42
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);
}
