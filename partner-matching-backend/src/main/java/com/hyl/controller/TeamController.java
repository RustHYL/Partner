package com.hyl.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.common.BaseResponse;
import com.hyl.common.DeleteRequest;
import com.hyl.common.ErrorCode;
import com.hyl.common.ResultUtils;
import com.hyl.exception.BusinessException;
import com.hyl.model.dto.TeamQuery;
import com.hyl.model.entity.Team;
import com.hyl.model.entity.User;
import com.hyl.model.entity.UserTeam;
import com.hyl.model.request.TeamAddRequest;
import com.hyl.model.request.TeamJoinRequest;
import com.hyl.model.request.TeamQuitRequest;
import com.hyl.model.request.TeamUpdateRequest;
import com.hyl.model.vo.TeamUserVO;
import com.hyl.service.TeamService;
import com.hyl.service.UserService;
import com.hyl.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/team")
@Slf4j
@CrossOrigin(origins = {"http://127.0.0.1:5173","http://localhost:5173","http://127.0.0.1:8080","http://localhost:8080"})
public class TeamController {


    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request){
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = BeanUtil.copyProperties(teamAddRequest, Team.class);
        long result = teamService.addTeam(team, loginUser);
        return ResultUtils.success(team.getId());

    }



    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request){
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        boolean update = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"队伍更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id){
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

//    @GetMapping("/list")
//    public BaseResponse<List<Team>> getTeamList(@RequestParam TeamQuery teamQuery){
//        if (teamQuery == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        Team team = BeanUtil.copyProperties(teamQuery, Team.class);
//        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
//        List<Team> list = teamService.list(queryWrapper);
//        return ResultUtils.success(list);
//    }


    //todo 判定是否是自己加入、创建的队伍，并给一个状态
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> getTeamList(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        //1.查询队伍列表
        List<TeamUserVO> list = teamService.listTeams(teamQuery, isAdmin);
        final List<Long> teamIdList = list.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        try {
            User loginUser = userService.getLoginUser(request);
            queryWrapper.eq("user_id",loginUser.getId());
            queryWrapper.in("team_id",teamIdList);
            List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
            Set<Long> hasJoinTeamSet = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            list.forEach(team ->{
                boolean hasJoin = hasJoinTeamSet.contains(team.getId());
                team.setHasJoin(hasJoin);
            });
        } catch (Exception e){

        }
        return ResultUtils.success(list);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> getTeamListByPage(TeamQuery teamQuery){
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = BeanUtil.copyProperties(teamQuery, Team.class);
        int pageSize = teamQuery.getPageSize();
        long current = teamQuery.getPageNum();
        Page<Team> page = new Page<>(current,pageSize);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(teamPage);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request){
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request){
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        boolean delete = teamService.deleteTeam(id, loginUser);
        if (!delete){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"队伍删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取我创建的队伍
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/create")
    public BaseResponse<List<TeamUserVO>> getCreateTeamList(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());
        List<TeamUserVO> list = teamService.listTeams(teamQuery, true);
        return ResultUtils.success(list);
    }

    /**
     * 获取我加入的队伍
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/join")
    public BaseResponse<List<TeamUserVO>> getJoinTeamList(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        //取出全部id（注意脏数据）
        Map<Long, List<UserTeam>> listMap = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        ArrayList<Long> idList = new ArrayList<>(listMap.keySet());
        teamQuery.setIdList(idList);
        List<TeamUserVO> list = teamService.listTeams(teamQuery, true);
        return ResultUtils.success(list);
    }

}
