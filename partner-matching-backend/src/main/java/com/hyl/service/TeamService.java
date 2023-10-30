package com.hyl.service;

import com.hyl.model.dto.TeamQuery;
import com.hyl.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.model.entity.User;
import com.hyl.model.request.TeamUpdateRequest;
import com.hyl.model.vo.TeamUserVO;

import java.util.List;

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

    /**
     * 搜索队伍列表
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);
}
