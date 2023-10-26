package com.hyl.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.common.BaseResponse;
import com.hyl.common.ErrorCode;
import com.hyl.common.ResultUtils;
import com.hyl.exception.BusinessException;
import com.hyl.model.dto.TeamQuery;
import com.hyl.model.entity.Team;
import com.hyl.model.entity.User;
import com.hyl.model.request.TeamAddRequest;
import com.hyl.service.TeamService;
import com.hyl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/team")
@Slf4j
@CrossOrigin(origins = {"http://127.0.0.1:5173","http://localhost:5173","http://127.0.0.1:8080","http://localhost:8080"}, allowCredentials = "true")
public class TeamController {


    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestParam TeamAddRequest teamAddRequest, HttpServletRequest request){
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = BeanUtil.copyProperties(teamAddRequest, Team.class);
        long result = teamService.addTeam(team, loginUser);
        return ResultUtils.success(team.getId());

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestParam long id){
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean delete = teamService.removeById(id);
        if (!delete){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"队伍删除失败");
        }
        return ResultUtils.success(true);
    }

    @PutMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestParam Team team){
        if (!(team != null)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean update = teamService.updateById(team);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"队伍更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(@RequestParam long id){
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<Team>> getTeamList(@RequestParam TeamQuery teamQuery){
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = BeanUtil.copyProperties(teamQuery, Team.class);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> list = teamService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> getTeamListByPage(@RequestParam TeamQuery teamQuery){
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

}
