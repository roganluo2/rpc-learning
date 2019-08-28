package com.my.rpc.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class ZKRegistryCenter implements IRegistryCenter {

    private CuratorFramework curatorFramework;


    {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZKConfig.CONNECT_STR).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("zkRegistry")
                .build();
        curatorFramework.start();
    }

    @Override
    public void registry(String serviceName, String serviceAddress) {
        String servicePath = "/" + serviceName;
        try {
            if(curatorFramework.checkExists().forPath(servicePath) == null)
            {
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            String addressPath = servicePath + "/" + serviceAddress;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            System.out.println("服务注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
