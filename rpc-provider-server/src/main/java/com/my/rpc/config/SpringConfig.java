package com.my.rpc.config;

import com.my.rpc.server.bio.RpcServer;
import com.my.rpc.server.netty.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


/**
 * @Description TODO
 * @Date 2019/7/4 12:52
 * @Created by rogan.luo
 */
@Configuration
public class SpringConfig {

    /*@Bean(initMethod = "start")
    public RpcServer rpcServer(){
        return new RpcServer(8080);
    }*/


    @Bean(initMethod = "start")
    public NettyServer nettyServer(){
        return new NettyServer(8080);
    }


}
