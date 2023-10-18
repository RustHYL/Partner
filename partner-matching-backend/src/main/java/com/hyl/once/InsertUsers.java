package com.hyl.once;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


import com.hyl.mapper.UserMapper;
import com.hyl.model.entity.User;
import com.hyl.service.UserService;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

@Component
public class InsertUsers {

    @Resource
    private UserService userService;

    //第一个参数默认运行多少个线程池，第二个最大的线程池数量，第三个线程存活时间，第四个单位，第五个任务队列,在任务数量超过指定任务数量的时候会增加线程池的数量，如果线程池数量满了，会按照第六个参数执行指定的策略，默认是终端策略
    private ExecutorService executorService = new ThreadPoolExecutor(60,1000,10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 批量插入用户
     */
    public void doInsert(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 10000000;
        List<User> userList = new ArrayList<User>();
        for (int i = 0; i < INSERT_NUM; i++){
            User user = new User();
            user.setUsername("");
            user.setUserAccount("");
            user.setAvatarUrl("");
            user.setGender(0);
            user.setUserPassword("");
            user.setPhone("");
            user.setEmail("");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setVerifyCode("");
            user.setTags("");
            user.setProfile("");
            userList.add(user);
        }
        userService.saveBatch(userList, 30);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


    /**
     * 并发批量插入用户（插入顺序无所谓，且没有包含非并发类的集合）
     */
    public void doConcurrencyInsert(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        int num = 0;
        //异步任务数组
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            List<User> userList = Collections.synchronizedList(new ArrayList<User>());
            while (true){
                num++;
                User user = new User();
                user.setUsername("");
                user.setUserAccount("");
                user.setAvatarUrl("");
                user.setGender(0);
                user.setUserPassword("");
                user.setPhone("");
                user.setEmail("");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setVerifyCode("");
                user.setTags("");
                user.setProfile("");
                userList.add(user);
                if(num % 10000 == 0){
                    break;
                }
            }
            //java默认的线程池
//            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
//                userService.saveBatch(userList, 10000);
//            });
            //使用自己创建的线程池
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                userService.saveBatch(userList, 10000);
            },executorService);
            //添加到异步数组
            futureList.add(voidCompletableFuture);
        }
        //不加join会使得程序依然是异步的，程序刚开始就可能结束了，join的阻塞使得在所有异步操作执行完成后结束
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
