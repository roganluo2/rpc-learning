package com.my.rpc;

import com.my.rpc.server.bio.RpcServer;
import com.my.rpc.server.netty.NettyServer;
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
        NettyServer nettyServer = applicationContext.getBean("nettyServer", NettyServer.class);
        nettyServer.start(8080);

    }
}
