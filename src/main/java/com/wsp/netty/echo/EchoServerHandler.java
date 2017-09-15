package com.wsp.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * echo 服务器处理类
 * @author wsp
 * @since 2017/09/14
 */
@ChannelHandler.Sharable //一个channelHandler可以被多个channel安全的共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
    /**
     * 接受channel对应的IO读入的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received:  "+in.toString(CharsetUtil.UTF_8));
        ctx.write(in);//将客户端发送的数据回传回客户端
    }
}
