package com.hyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.hyl.common.ErrorCode;
import com.hyl.exception.BusinessException;
import com.hyl.model.entity.User;
import com.hyl.model.vo.UserVO;
import com.hyl.service.UserService;
import com.hyl.mapper.UserMapper;
import com.hyl.utils.AlgorithmUtils;
import com.hyl.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.bytebuddy.description.method.MethodDescription;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author Alan
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-09-14 13:49:12
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{

    @Resource
    private UserMapper userMapper;


    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param verifyCode 校验账号
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String verifyCode) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, verifyCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能小于8位");
        }
        if (verifyCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验账号不能大于5位");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码与校验密码不一致");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("verify_code", verifyCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验重复");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((Constant.SALT + userPassword).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setVerifyCode(verifyCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户数据保存失败");
        }
        return user.getId();
    }



    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能小于8位");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((Constant.SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4. 记录用户的登录态
        request.getSession().setAttribute(Constant.USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setVerifyCode(originUser.getVerifyCode());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setTags(originUser.getTags());
        safetyUser.setProfile(originUser.getProfile());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(Constant.USER_LOGIN_STATE);
        return 1;
    }


    /**
     * 按照标签搜索永固
     * @param tagList 标签列表
     * @return
     */
    @Override
    public List<User> searchUserByTags(List<String> tagList){
        if (CollectionUtils.isEmpty(tagList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //数据库查询
//        QueryWrapper queryWrapper = new QueryWrapper<>();
//        //拼接and查询
//        for (String tagName: tagList) {
//            queryWrapper.like("tags", tagName);
//        }
//        List<User> list = userMapper.selectList(queryWrapper);
        //内存查询
        //1.先查询全部
        QueryWrapper queryWrapper = new QueryWrapper<>();
        List<User> list = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        //2.内存中判断是否包含要求的标签（灵活）
        return list.stream().filter(user -> {
            String tagStr = user.getTags();
            if(StringUtils.isBlank(tagStr)){
                return false;
            }
            Set<String> tempTagNameSet = gson.fromJson(tagStr, new TypeToken<Set<String>>(){}.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagList){
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
//        return list.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //如果前端传入的user只有id属性直接error
        if (StringUtils.isAllBlank(user.getUsername(),user.getAvatarUrl(),user.getPhone(),user.getEmail())
                && user.getGender() == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        //如果是管理员可以更新任意用户
        if (isAdmin(loginUser) && userId != loginUser.getId()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null){
            return null;
        }
        Object userObj = request.getSession().getAttribute(Constant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return (User) userObj;
    }


    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(Constant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == Constant.ADMIN_ROLE;
    }

    /**
     * 是否为管理员(loginUser)
     * @param loginUser
     * @return
     */
    @Override
    public boolean isAdmin(User loginUser) {
        // 仅管理员可查询
        return loginUser != null && loginUser.getUserRole() == Constant.ADMIN_ROLE;
    }

    @Override
    public List<User> matchUser(long num, User loginUser) {
        List<User> userList = this.list();
        String loginUserTags = loginUser.getTags();
        Gson gson = new Gson();
        List<String> loginUserTagsList = gson.fromJson(loginUserTags, new TypeToken<List<String>>() {
        }.getType());
        SortedMap<Integer, Long> similarityMap = new TreeMap<>();
        int i;
        for (i = 0; i < userList.size(); i++){
            User user = userList.get(i);
            String userTags = user.getTags();
            if (StringUtils.isBlank(userTags)) {
                continue;
            }
            List<String> userTagsList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            long score = AlgorithmUtils.minDistance(loginUserTagsList, userTagsList);
            similarityMap.put(i, score);
        }
        List<Integer> similarUserList = similarityMap.keySet().stream().limit(num).collect(Collectors.toList());
        List<User> indexSimilarList = similarUserList.stream().map(index -> getSafetyUser(userList.get(index))).collect(Collectors.toList());
//        for (Integer index : similarUserList) {
//            User safetyUser = getSafetyUser(userList.get(index));
//            userVOList.add(BeanUtil.copyProperties(safetyUser, UserVO.class));
//        }
        return indexSimilarList;
    }


}





