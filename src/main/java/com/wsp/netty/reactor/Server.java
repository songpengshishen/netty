package com.wsp.netty.reactor;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by wangsongpeng on 2018/1/11.
 */
public class Server {

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel =  ServerSocketChannel.open();
        ServerSocket socket =  serverSocketChannel.socket();
        socket.bind(new InetSocketAddress(2002));
        Socket socket1  =  socket.accept();
        SocketChannel socketChannel =   socket1.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        int count =  socketChannel.read(buffer);
        if(count > 0){
            byte[] bytes = new byte[count];
            buffer.flip();
            buffer.get(bytes);
            System.out.println(new String(bytes));
        }

    }
}
