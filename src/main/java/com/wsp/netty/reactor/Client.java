package com.wsp.netty.reactor;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by wangsongpeng on 2018/1/11.
 */
public class Client {
    public static void main(String[] args) throws Exception{
        SocketChannel clienSocketChannel =  SocketChannel.open();//客户端管道
        clienSocketChannel.configureBlocking(false);//设置为非阻塞
        Selector selector = Selector.open();//多路复用器
        boolean connected =  clienSocketChannel.connect(new InetSocketAddress("127.0.0.1",8000));
        if(connected){
            //如果已经连接成功,则直接写数据
            write(clienSocketChannel,selector);
        }else{
            //如果没有连接成功,注册多路复用器
            clienSocketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

        while(true){
            try {
                selector.select();
                Set<SelectionKey> keys =  selector.selectedKeys();
                if(null != keys && !keys.isEmpty()){
                    Iterator<SelectionKey> it =  keys.iterator();
                    while (it.hasNext()){
                         SelectionKey key = it.next();
                         it.remove();
                         SocketChannel socketChannel = (SocketChannel)key.channel();
                         if(key.isConnectable()){
                             socketChannel.finishConnect();
                             socketChannel.configureBlocking(false);
                             socketChannel.write(ByteBuffer.wrap(new String("向服务端发送了一条信息").getBytes()));
                             socketChannel.register(selector,SelectionKey.OP_READ);
                         }else if(key.isReadable()){
                             read(socketChannel,selector);
                         }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private static void write(SocketChannel channel,Selector selector)throws Exception{
        String sendMsg = new String("Hello Netty Server!");
        ByteBuffer byteBuffer = ByteBuffer.allocate(3 * sendMsg.length());
        byteBuffer.put(sendMsg.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.register(selector,SelectionKey.OP_READ);
    }


    private static void read(SocketChannel channel,Selector selector)throws Exception{
        ByteBuffer buffer = ByteBuffer.allocate(100);
        channel.read(buffer);
        byte[] bytes = buffer.array();
        System.out.println(new String(bytes));
    }
}
