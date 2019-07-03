package com.my.rpc.user;

import com.my.rpc.entity.User;
import com.my.rpc.service.UserService;

/**
 * @Description TODO
 * @Date 2019/7/3 12:52
 * @Created by rogan.luo
 */
public class UserServiceImp implements UserService {
    public String save(User user) {
        return "Hi " + user.getName();
    }

    public String getById(Integer id) {
        return "Success";
    }
}
