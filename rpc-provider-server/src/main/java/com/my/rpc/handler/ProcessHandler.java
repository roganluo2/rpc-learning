package com.my.rpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.my.rpc.protocol.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Description TODO
 * @Date 2019/7/3 14:36
 * @Created by rogan.luo
 */
@Data
@AllArgsConstructor
public class ProcessHandler implements Runnable{

    private Socket socket;

    private Object service;

    public Object handle(RpcRequest rpcRequest){
        try {
            String className = rpcRequest.getClassName();
            Class<?> aClass = Class.forName(className);

            Object[] params = rpcRequest.getParams();
            Class[] paramClasses = null;
            if(params != null && params.length > 0)
            {
                paramClasses = new Class[params.length];
                 for(int i=0;i<params.length;i++)
                 {
                     paramClasses[i] = params[i].getClass();
                 }
            }
            Method method = aClass.getMethod(rpcRequest.getMethodName(), paramClasses);
            return method.invoke(service, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object handle = this.handle(rpcRequest);
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
