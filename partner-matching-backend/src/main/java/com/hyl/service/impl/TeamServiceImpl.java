package com.hyl.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.common.ErrorCode;
import com.hyl.exception.BusinessException;
import com.hyl.model.dto.TeamQuery;
import com.hyl.model.entity.Team;
import com.hyl.model.entity.User;
import com.hyl.model.entity.UserTeam;
import com.hyl.model.enums.TeamStatusEnum;
import com.hyl.model.request.TeamJoinRequest;
import com.hyl.model.request.TeamQuitRequest;
import com.hyl.model.request.TeamUpdateRequest;
import com.hyl.model.vo.TeamUserVO;
import com.hyl.model.vo.UserVO;
import com.hyl.service.TeamService;
import com.hyl.mapper.TeamMapper;
import com.hyl.service.UserService;
import com.hyl.service.UserTeamService;
import com.hyl.utils.Constant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id >= 1){
                queryWrapper.eq("id", id);
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)){
                queryWrapper.like("name",name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)){
                queryWrapper.like("description",description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum >=1){
                queryWrapper.eq("max_num", maxNum);
            }
            Long userId = teamQuery.getUserId();
            //根据创建人查询
            if (userId != null && userId >=1){
                queryWrapper.eq("user_id", userId);
            }

            //关键词查询（查询名字和详情）
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)){
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description",searchText));
            }
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);

//            if (statusEnum == null){
//                statusEnum = TeamStatusEnum.PUBLIC;
//            }
//            if (!isAdmin && !statusEnum.equals(TeamStatusEnum.PUBLIC)) {
//                throw new BusinessException(ErrorCode.NO_AUTH);
//            }
//            queryWrapper.eq("status", statusEnum.getValue());

            if (!isAdmin){
                if (statusEnum == null) {
                    statusEnum = TeamStatusEnum.PUBLIC;
                    queryWrapper.eq("status", statusEnum.getValue());
                }else if (!statusEnum.equals(TeamStatusEnum.PUBLIC)){
                    throw new BusinessException(ErrorCode.NO_AUTH);
                }else {
                    queryWrapper.eq("status", statusEnum.getValue());
                }
            }else {
                if (statusEnum != null) {
                    queryWrapper.eq("status", statusEnum.getValue());
                }
            }
        }else {
            if (!isAdmin){
                TeamStatusEnum statusEnum = TeamStatusEnum.PUBLIC;
                queryWrapper.eq("status", statusEnum.getValue());
            }
        }
        //不查询已过期的队伍
        queryWrapper.and(qw -> qw.gt("expire_time", new Date()).or().isNull("expire_time"));


        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)){
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        //关联查询创建人用户信息
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = BeanUtil.copyProperties(team, TeamUserVO.class);
            if (user != null){
                UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        Team oldTeam = getTeamById(id);
        //管理员或者创建队伍的人能修改信息
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(oldTeam.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //状态修改为加密需要密码
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (teamStatusEnum.equals(TeamStatusEnum.SECRET)){
            if (StringUtils.isBlank(teamUpdateRequest.getPassword()) && StringUtils.isBlank(oldTeam.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密状态必须设置密码");
            }
        }
        Team updateTeam = new Team();
        updateTeam = BeanUtil.copyProperties(teamUpdateRequest,Team.class);
        return this.updateById(updateTeam);
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamJoinRequest.getId();
        Team team = getTeamById(teamId);
        //只能加入未过期
        Date expireTime = team.getExpireTime();
        if (expireTime == null || expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        //私密队伍无法加入（邀请）
        Integer status = team.getStatus();
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(teamStatusEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无法私密队伍");
        }
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(teamStatusEnum)){
            if (StringUtils.isBlank(password) || !teamJoinRequest.getPassword().equals(password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不正确");
            }
        }
        //队伍加入限制个数
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        int count = userTeamService.count(queryWrapper);
        if (count >= Constant.PERSONAL_TEAM_MAX_COUNT){
            throw new BusinessException(ErrorCode.NO_AUTH, "加入或已有队伍数量已满");
        }
        //不能重复加入已加入的队伍
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id",teamId);
        queryWrapper.eq("user_id", userId);
        int userJoinCount = userTeamService.count(queryWrapper);
        if (userJoinCount >= 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已经加入该队伍");
        }
        //加入未满的
        long memberCount = this.countTeamUserByTeamId(teamId);
        if (memberCount >= team.getMaxNum()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "加入队伍已满");
        }
        //添加到user-team关联中
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());

        return userTeamService.save(userTeam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getId();
        Team team = getTeamById(teamId);
        //校验我在队伍中
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("team_id", teamId);
        int count = userTeamService.count(queryWrapper);
        if (count == 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求队伍错误，未在队伍中");
        }
        long memberCount = this.countTeamUserByTeamId(teamId);
        //队伍只剩一人，解散队伍
        if (memberCount == 1) {
            this.removeById(teamId);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("team_id", teamId);
        } else {
            //是否是队长
            if (userId.equals(team.getUserId())) {
                //转移队长(转移给第二早加入的队员)，删除user-team关系
                //查询第二加入的成员
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("team_id", teamId);
                queryWrapper.last("order by asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取关系列表失败");
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextLeaderId = nextUserTeam.getUserId();
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队长失败");
                }
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                queryWrapper.eq("team_id", teamId);
            }
        }
        return userTeamService.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(long id, User loginUser) {
        //校验请求参数
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验队伍是否存在
        Team team = getTeamById(id);
        //校验你是不是队长或者管理员
        Long userId = loginUser.getId();
        if (!userId.equals(team.getUserId()) || !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH, "无访问权限");
        }
        //移除所有加入队伍的关联信息
        Long teamId = team.getId();
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        boolean result = userTeamService.remove(queryWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败");
        }
        //删除队伍
        return this.removeById(teamId);
    }

    /**
     * 获取队伍信息
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "队伍不存在");
        }
        return team;
    }



    /**
     * 获取某队伍的人数
     * @param teamId
     * @return
     */
    private long countTeamUserByTeamId(long teamId){
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        return userTeamService.count(queryWrapper);
    }
}




