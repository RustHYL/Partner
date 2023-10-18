package com.hyl.service;

import com.hyl.model.entity.User;
import com.hyl.utils.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class InsertTest {

    @Resource
    private UserService userService;

    //第一个参数默认运行多少个线程池，第二个最大的线程池数量，第三个线程存活时间，第四个单位，第五个任务队列,在任务数量超过指定任务数量的时候会增加线程池的数量，如果线程池数量满了，会按照第六个参数执行指定的策略，默认是终端策略
    private ExecutorService executorService = new ThreadPoolExecutor(60,1000,10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 批量插入用户
     * 32088毫秒10w
     */
    @Test
    public void doInsert(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<User>();
        for (int i = 0; i < INSERT_NUM; i++){
            User user = new User();
            user.setUsername("testUser");
            user.setUserAccount("testuser");
            user.setAvatarUrl("https://c-ssl.duitang.com/uploads/item/201810/25/20181025171436_QxNs2.jpeg");
            user.setGender(0);
            user.setUserPassword(DigestUtils.md5DigestAsHex((Constant.SALT + "12345678").getBytes()));
            user.setPhone("12345678");
            user.setEmail("xxx@xxx.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setVerifyCode("111111");
            user.setTags("[]");
            user.setProfile("测试数据");
            userList.add(user);
        }
        userService.saveBatch(userList, 30);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


    /**
     * 并发批量插入用户（插入顺序无所谓，且没有包含非并发类的集合）
     * 10457毫秒10w
     */
    @Test
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
                user.setUsername("testUser");
                user.setUserAccount("testuser");
                user.setAvatarUrl("https://c-ssl.duitang.com/uploads/item/201810/25/20181025171436_QxNs2.jpeg");
                user.setGender(0);
                user.setUserPassword(DigestUtils.md5DigestAsHex((Constant.SALT + "12345678").getBytes()));
                user.setPhone("12345678");
                user.setEmail("xxx@xxx.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setVerifyCode("111111");
                user.setTags("[]");
                user.setProfile("测试数据");
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
