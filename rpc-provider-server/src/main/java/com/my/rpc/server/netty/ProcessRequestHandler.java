package com.my.rpc.server.netty;

import com.alibaba.fastjson.JSONObject;
import com.my.rpc.handler.RpcHander;
import com.my.rpc.protocol.RpcReponse;
import com.my.rpc.protocol.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;

import java.util.Map;


/**
 * @Description
 * @Date 2019/7/10 13:41
 * @Created by rogan.luo
 */

@AllArgsConstructor
public class ProcessRequestHandler  extends ChannelInboundHandlerAdapter {

    private Map<String,Object> serviceMap;

    //获取数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String requestStr = new String(bytes, "utf-8");
        //转换成对象
        RpcRequest rpcRequest = JSONObject.parseObject(requestStr, RpcRequest.class);
        Object handle = new RpcHander(rpcRequest, serviceMap).handle();
        RpcReponse rpcReponse = new RpcReponse();
        rpcReponse.setId(rpcRequest.getId());
        rpcReponse.setObj(handle);
        ctx.channel().write(JSONObject.toJSONString(rpcReponse));
    }

    //处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
