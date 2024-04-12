package org.meteor.metty.api.service;

import org.meteor.metty.api.pojo.User;

import java.util.List;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: HelloService
 * @Created Time: 2024-04-09 15:52
 **/
public interface UserService {

    User queryUser();

    List<User> getAllUsers();

}
