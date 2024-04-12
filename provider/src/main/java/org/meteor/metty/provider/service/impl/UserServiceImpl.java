package org.meteor.metty.provider.service.impl;

import org.meteor.metty.api.pojo.User;
import org.meteor.metty.api.service.UserService;
import org.meteor.metty.server.annotation.RpcService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: UserServiceImpl
 * @Created Time: 2024-04-09 16:13
 **/


@RpcService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public User queryUser() {
        return new User("hwd", "123456", 25);
    }

    @Override
    public List<User> getAllUsers() {
        // 注意：直接使用 Arrays.ArrayList 会导致序列化异常
        return new ArrayList<>(Arrays.asList(new User("xm", "123456", 23),
                new User("hwd", "123456", 23),
                new User("hwd", "123456", 24)));
    }
}
