package com.wsp.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * echo客户端
 * @author wsp
 * @since 2017/09/17
 */
public class EchoClient {
    private final String host = "127.0.0.1";
    private final int port = 2101;

    /**
     * echo客户端连接echo服务器
     * @throws Exception
     */
    public void connection() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();//netty 客户端类
            bootstrap.group(group)
                     .channel(NioSocketChannel.class) //使用Niosocket管道
                     .remoteAddress(new InetSocketAddress(host,port)) //连接远程服务器
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             socketChannel.pipeline().addLast(new EchoClientHandler());//绑定客户端管道处理类
                         }
                     });
                    ChannelFuture f = bootstrap.connect().sync();//阻塞连接服务端
                    f.channel().closeFuture().sync();
        }finally {
                group.shutdownGracefully().sync();
        }
    }
}
