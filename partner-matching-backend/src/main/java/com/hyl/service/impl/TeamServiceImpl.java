package com.hyl.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.common.ErrorCode;
import com.hyl.exception.BusinessException;
import com.hyl.mapper.UserTeamMapper;
import com.hyl.model.entity.Team;
import com.hyl.model.entity.User;
import com.hyl.model.entity.UserTeam;
import com.hyl.model.enums.TeamStatusEnum;
import com.hyl.service.TeamService;
import com.hyl.mapper.TeamMapper;
import com.hyl.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
* @author Alan
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2023-10-26 13:40:42
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private UserTeamService userTeamService;

    @Override
    public long addTeam(Team team, User loginUser) {
        //  1. 请求参数是否为空？
        if(team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //  2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        //  3. 校验信息
        //  1. 队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //  2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //  3. 描述 <= 512
        String desc = team.getDescription();
        if (StringUtils.isBlank(desc) && desc.length() > 512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不满足要求");
        }
        //  4. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (teamStatusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //  5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(teamStatusEnum) && (StringUtils.isBlank(password) || password.length() > 32)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不正确");
        }
        //  6. 超时时间 > 当前时间
        if (new Date().after(team.getExpireTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间不能小于当前时间");
        }
        //  7. 校验用户最多创建 5 个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        long teamCount = this.count(queryWrapper);
        if (teamCount >= 5) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍数量不能超过5个");
        }

        return this.saveOnly(team,loginUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public long saveOnly(Team team, User loginUser){
        //  4. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(loginUser.getId());
        boolean saveTeam = this.save(team);
        long teamId = team.getId();
        if (!saveTeam) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }
        //  5. 插入用户  => 队伍关系到关系表

        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(loginUser.getId());
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());

        boolean saveRelationship = userTeamService.save(userTeam);
        if (!saveRelationship) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍-成员关系失败");
        }
        return teamId;
    }
}




