package com.my.rpc.handler;

import com.my.rpc.entity.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2019/7/3 14:36
 * @Created by rogan.luo
 */
@Data
@AllArgsConstructor
public class ProcessHandler implements Runnable{

    private Socket socket;

    private Map<String, Object> serviceMap;


    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object handle = new RpcHander(rpcRequest, serviceMap).handle();
            outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(handle);
            outputStream.flush();

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
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
