package com.wsp.netty.reactor;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 事件接受者,处理IO连接的Accept
 * @author wsp
 * @since 2018/01/09
 */
public class Acceptor implements Invoke{



    protected final Selector selector;
    protected final ServerSocketChannel serverChannel;

    public Acceptor(Selector selector, ServerSocketChannel serverChannel){
        this.selector = selector;
        this.serverChannel = serverChannel;
    }

    @Override
    public void invoke() {
        try {
            SocketChannel clientChannel = serverChannel.accept();
            if(null != clientChannel){
                System.out.println("连接成功!");
                new Handler(selector,clientChannel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws  Exception{
        SocketChannel socketChannel =    SocketChannel.open();
        socketChannel.socket().connect(new InetSocketAddress(2001));
        ByteBuffer buffer =   ByteBuffer.allocate(100);
        buffer.clear();
        String str = new String("Hello Netty!");
        buffer.put(str.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()){
            socketChannel.write(buffer);
        }
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();
        byte[] b1 = new byte[buffer.limit()];
        buffer.get(b1);
        System.out.println(new String(b1));
    }
}
