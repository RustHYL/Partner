package com.hyl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class partner-matching-backendApplicationTests {


    @Resource
    private UserService userService;
    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("yep");
        user.setUserAccount("123456");
        user.setAvatarUrl("https://images.zsxq.com/FrXjrhhVzNyFljg4foXvv7tcmuTK?e=1698767999&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:LgyxeEzUGjYCzOg0H5tJ3GXy8_s=");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("17815700343");
        user.setEmail("1257635375@qq.com");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

}
