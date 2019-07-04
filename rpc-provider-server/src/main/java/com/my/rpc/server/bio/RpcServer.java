package com.my.rpc.server.bio;

import com.my.rpc.anno.APIInfo;
import com.my.rpc.handler.ProcessHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 接收端
 * @Date 2019/7/3 12:53
 * @Created by rogan.luo
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Map<String,Object> serviceMap = new HashMap<String,Object>();


    public void start(int port){
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true)
            {
                socket = serverSocket.accept();
                executorService.execute(new ProcessHandler(socket,serviceMap ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null)
            {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket != null)
            {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null)
            {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //注册bean
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(APIInfo.class);
        Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();
        for(Map.Entry<String,Object> e:entries)
        {
            APIInfo annotation = e.getValue().getClass().getAnnotation(APIInfo.class);
            serviceMap.put(annotation.value().getName(), e.getValue());
        }
    }

    private ApplicationContext applicationContext;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
         this.applicationContext = applicationContext;
    }
}
