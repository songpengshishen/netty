package com.wsp.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 基于原生 JDK NIO API的Server,没有线程池概念无法并行执行
 * @author wsp
 */
public class JdkNioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket serverSocket = serverChannel.socket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1",20001));//绑定端口
        Selector selector = Selector.open();//打开选择器处理channel
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer byteBuffer = ByteBuffer.wrap("HI!\r\n".getBytes());
        for(;;){
            try {
                selector.select();
            }catch (IOException e){
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys =  selector.selectedKeys();
            Iterator<SelectionKey> it = readyKeys.iterator();
            while (it.hasNext()){
                 SelectionKey key =  it.next();
                 it.remove();
                 try{
                     if(key.isAcceptable()){
                         ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                         SocketChannel clientChannel =  channel.accept();
                         clientChannel.configureBlocking(false);
                         clientChannel.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ,byteBuffer.duplicate());
                         System.out.println("accepted connection from " +clientChannel);
                     }
                     if(key.isWritable()){
                         ///写不下去了...
                     }
                 }catch (Exception e){
                     e.printStackTrace();
                 }
            }
        }
    }
}
