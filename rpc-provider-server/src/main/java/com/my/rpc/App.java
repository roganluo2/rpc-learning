package com.my.rpc;

import com.my.rpc.server.bio.RpcServer;
import com.my.rpc.user.UserServiceImp;

/**
 * @Description TODO
 * @Date 2019/7/3 20:59
 * @Created by rogan.luo
 */
public class App {

    public static void main(String[] args) {

        new RpcServer(new UserServiceImp(),8080).start();
        System.out.println("服务端启动成功！！！");

    }
}
