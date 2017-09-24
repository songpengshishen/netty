package com.wsp.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Echo服务器
 * @author wsp
 * @since 2017/09/14
 */
public class EchoServer {
    private final int port = 2001;

    public EchoServer(){

    }

    /**
     * 启动echo服务器
     * @throws Exception
     */
    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup loopGroup = new NioEventLoopGroup();//创建eventLoopGroup
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();//创建ServerBootStrap,netty服务端类
            bootstrap.group(loopGroup)
                    .channel(NioServerSocketChannel.class) //指定使用NioServerSocketChannel传输Channel
                    .localAddress(new InetSocketAddress(port)) //监听本机指定端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { //将echoServerHandler添加到channel中
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();//异步绑定服务器
            future.channel().closeFuture().sync();
        }finally {
            loopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)throws Exception {
        EchoServer es = new EchoServer();
        es.start();
    }
}
