package com.my.rpc.handler;

import com.my.rpc.protocol.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2019/7/10 14:11
 * @Created by rogan.luo
 */
@AllArgsConstructor
public class RpcHander {

    private RpcRequest rpcRequest;

    private Map serviceMap;


    public Object handle(){
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
            Object service = serviceMap.get(className);
            return method.invoke(service, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
