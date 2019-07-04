package com.my.rpc;

import com.my.rpc.server.bio.RpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description TODO
 * @Date 2019/7/3 20:59
 * @Created by rogan.luo
 */
@ComponentScan(basePackages = "com.my.rpc")
public class App {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        RpcServer rpcServer = applicationContext.getBean("rpcServer", RpcServer.class);
        rpcServer.start(8080);

    }
}
