package com.wsp.netty.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * netty api 编写的OIO(阻塞IO)服务器
 * @author wsp
 */
public class NettyOioServer {
    public static void main(String[] args) throws Exception{
        final ByteBuf buffer = Unpooled.unmodifiableBuffer(Unpooled.copiedBuffer("HI\r\n",Charset.forName("UTF-8")));
        EventLoopGroup loopGroup = new OioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(loopGroup).channel(OioServerSocketChannel.class).localAddress(new InetSocketAddress(20001))
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel socketChannel) throws Exception {
                         socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                             @Override
                             public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                 ctx.writeAndFlush(buffer.duplicate()).addListener(ChannelFutureListener.CLOSE);
                             }
                         });
                     }
                 });
         ChannelFuture future =  bootstrap.bind().sync();
         future.channel().closeFuture().sync();
    }
}
