package com.hyl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.model.entity.UserTeam;
import com.hyl.service.UserTeamService;
import com.hyl.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author Alan
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2023-10-26 13:45:40
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




