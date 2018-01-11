package com.wsp.netty.reactor;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by wangsongpeng on 2018/1/11.
 */
public class Client {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel =    SocketChannel.open();
        socketChannel.socket().connect(new InetSocketAddress(2002));
        ByteBuffer buffer =   ByteBuffer.allocate(100);
        buffer.clear();
        String str = new String("Hello Netty!");
        buffer.put(str.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()){
            socketChannel.write(buffer);
        }
    }

}
