package com.wsp.netty.handler.server;

import com.wsp.netty.handler.DateUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Netty中处理出站消息的handler.
 * 该handler位于整个channel链的出站倒数第一的位置
 */
public class OutboundHandler1 extends ChannelOutboundHandlerAdapter {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutboundHandler1 write! " + DateUtils.getNowDateTimeStr() + " " + Thread.currentThread());
        String sendMsg = "I am ok";
        ByteBuf byteBuf = ctx.alloc().buffer(4 * sendMsg.length());
        byteBuf.writeBytes(sendMsg.getBytes());
        ctx.write(byteBuf);
        ctx.flush();
    }


}


