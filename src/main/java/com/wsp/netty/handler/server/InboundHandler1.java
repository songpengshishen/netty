package com.wsp.netty.handler.server;

import com.wsp.netty.handler.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Netty中处理入站消息的handler.
 * 该handler位于整个channel链的入站第一个位置
 */
public class InboundHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InboundHandler1 ChannelRead! " + DateUtils.getNowDateTimeStr() + " " + Thread.currentThread());
        ctx.fireChannelRead(msg);//通知执行下一个InboundHandler处理channelRead
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("InboundHandler1 channelReadComplete! " + DateUtils.getNowDateTimeStr() + " " + Thread.currentThread());
        ctx.flush();
    }
}


