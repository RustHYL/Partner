package com.hyl.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.model.entity.User;
import com.hyl.service.UserService;
import com.hyl.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    //重点用户
    private List<Long> mainUser = Arrays.asList(5L);
    //每天加载预热
    @Scheduled(cron = "59 59 23 * * *")
    public void doCacheRecommend(){
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        RLock lock = redissonClient.getLock(Constant.REDIS_RRECACHEJOB_PREFIX + "lock");
        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)){
                for (Long id : mainUser) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = Constant.REDIS_RECOMMEND_PREFIX + id;

                    //写缓存
                    try {
                        valueOperations.set(redisKey, userPage, Constant.REDIS_RECOMMEND_TIME, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        log.error("redis set key error");
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doRecommendUserCache error", e);
        } finally {
            //只可以释放自己的锁
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
