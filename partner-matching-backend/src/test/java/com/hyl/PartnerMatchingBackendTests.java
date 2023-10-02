package com.hyl;

import com.hyl.model.entity.User;
import com.hyl.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class PartnerMatchingBackendApplicationTests {


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

    @Test
    public void testSearchUserByTags(){
        List<String> tagList = Arrays.asList("java","python");
        List<User> userList = userService.searchUserByTags(tagList);
        System.out.println(userList.size());
    }


}
