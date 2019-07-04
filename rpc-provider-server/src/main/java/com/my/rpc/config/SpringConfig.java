package com.my.rpc.config;

import com.my.rpc.server.bio.RpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Description TODO
 * @Date 2019/7/4 12:52
 * @Created by rogan.luo
 */
@Configuration
public class SpringConfig {

    @Bean
    public RpcServer rpcServer(){
        return new RpcServer();
    }


}
