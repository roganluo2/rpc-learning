package com.my.rpc.service;

import com.my.rpc.entity.User;

/**
 * @Description 用户接口
 * @Date 2019/7/3 12:47
 * @Created by rogan.luo
 */
public interface UserService {

    String save(User user);

    String getById(Integer id);

}
