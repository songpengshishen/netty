package com.wsp.netty.reactor;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * reactor客户端 java Nio socket客户端
 */
public class Client implements Runnable{


    public static void main(String[] args) throws IOException{


    }



    public Client init() throws IOException{
        SelectorProvider selectorProvider = SelectorProvider.provider();

        /***
         * 客户端socket管道
         */
        SocketChannel socketChannel = selectorProvider.openSocketChannel();

        /**
         * 客户端socket
         */
        Socket socket = socketChannel.socket();

        /**
         * 多路复用器
         */
        Selector selector = selectorProvider.openSelector();

        socketChannel.configureBlocking(false);

        return this;
    }

    @Override
    public void run() {

    }



}
