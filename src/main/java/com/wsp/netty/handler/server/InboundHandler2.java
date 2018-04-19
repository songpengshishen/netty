package com.wsp.netty.handler.server;

import com.wsp.netty.handler.DateUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Netty中处理入站消息的handler.
 * 该handler位于整个channel链的入站第二个位置
 */
public class InboundHandler2 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InboundHandler2 ChannelRead! " + DateUtils.getNowDateTimeStr() + " " + Thread.currentThread());
        ByteBuf msgData = (ByteBuf) msg;
        byte[] byteArr = new byte[msgData.readableBytes()];
        msgData.readBytes(byteArr);
        String str = new String(byteArr);
        ctx.write(msg);
        msgData.release();

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("InboundHandler2 channelReadComplete! " + DateUtils.getNowDateTimeStr() + " " + Thread.currentThread());
        ctx.flush();
    }
}


