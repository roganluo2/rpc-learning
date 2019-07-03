package com.my.rpc.server.bio;

import com.alibaba.fastjson.JSONObject;
import com.my.rpc.handler.ProcessHandler;
import com.my.rpc.protocol.RpcRequest;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description 接收端
 * @Date 2019/7/3 12:53
 * @Created by rogan.luo
 */
public class RpcServer {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Object service;

    private Integer port;

    public RpcServer(Object service, Integer port) {
        this.service = service;
        this.port = port;
    }

    public void start(){
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true)
            {
                socket = serverSocket.accept();
                executorService.execute(new ProcessHandler(socket, service));
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

}
