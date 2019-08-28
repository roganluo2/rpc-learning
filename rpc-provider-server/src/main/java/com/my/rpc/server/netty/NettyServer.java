package com.my.rpc.server.netty;

import com.my.rpc.anno.APIInfo;
import com.my.rpc.registry.IRegistryCenter;
import com.my.rpc.registry.ZKRegistryCenter;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 使用netty做服务端
 * @Date 2019/7/10 11:19
 * @Created by rogan.luo
 */
public class NettyServer implements ApplicationContextAware, InitializingBean {

    private Map<String,Object> serviceMap = new HashMap<String,Object>();

    private IRegistryCenter registryCenter = new ZKRegistryCenter();

    int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start()
    {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //拿到pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        //添加编码解码处理器
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        pipeline.addLast(new ProcessRequestHandler(serviceMap));
                    }
                });
        try {
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //等待服务端监听关闭端口
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //不能关掉
//            boosGroup.shutdownGracefully();
//            workGroup.shutdownGracefully();
        }


    }

    //注册bean
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(APIInfo.class);
        Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();
        for(Map.Entry<String,Object> e:entries)
        {
            APIInfo annotation = e.getValue().getClass().getAnnotation(APIInfo.class);
            String key = annotation.value().getName();
            if(!StringUtils.isEmpty(annotation.version()))
            {
                key = key + "-" + annotation.version();
            }
            serviceMap.put(key, e.getValue());
            registryCenter.registry(key, getAddress() + ":" + port);
        }
    }



    private ApplicationContext applicationContext;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static String getAddress(){
        InetAddress inetAddress=null;
        try {
            inetAddress=InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddress.getHostAddress();// 获得本机的ip地址
    }
}
