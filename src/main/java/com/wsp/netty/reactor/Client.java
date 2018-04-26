package com.wsp.netty.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * reactor客户端 java Nio socket客户端
 */
public class Client implements Runnable{

    private SelectorProvider selectorProvider = SelectorProvider.provider();
    private SocketChannel socketChannel;
    private Selector selector;



    public Client init() throws IOException{
        /***
         * 客户端socket管道
         */
        socketChannel = selectorProvider.openSocketChannel();
        /**
         * 多路复用器
         */
        selector = selectorProvider.openSelector();
        socketChannel.configureBlocking(false);
        SelectionKey key =  socketChannel.register(selector, SelectionKey.OP_CONNECT);
        key.attach(this);
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8000));
        return this;
    }

    @Override
    public void run() {
        boolean stop = false;
        while (!stop){
           try {
                int keyCounts =  selector.select();
                if(keyCounts == 0){
                    continue;
                }
                Iterator<SelectionKey> keys =  selector.selectedKeys().iterator();
                while (keys.hasNext()){
                      SelectionKey key = keys.next();
                      SocketChannel channel =  (SocketChannel)key.channel();
                      if(key.isConnectable()){
                          if(channel.finishConnect()){
                              key.interestOps(SelectionKey.OP_READ);
                              channel.write(ByteBuffer.wrap(new String("Hello Reactor!").getBytes()));
                          }
                      }else if(key.isReadable()){
                           ByteBuffer buffer = ByteBuffer.allocate(200);
                           channel.read(buffer);
                           String str = new String(buffer.array());
                           System.out.println(Thread.currentThread().getName() + str);
                           stop = true;
                           channel.close();
                           selector.close();
                      }else{
                          key.cancel();
                          throw new IllegalStateException("No Support Channel Key!");
                      }
                      keys.remove();
                }
           }catch (Exception e){
               e.printStackTrace();
           }
        }
    }


    public static void main(String[] args) throws IOException{
         for(int i = 0;i<10;i++){
             new Thread(new Client().init()).start();
         }

    }
}
