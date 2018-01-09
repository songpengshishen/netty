package com.wsp.netty.reactor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wsp
 * @since 2018/01/09
 */
public class Reactor implements Runnable{

    /**
     * java Nio核心类,通过封装操作系统IO多路复用模型的select epoll poll系统调用来实现监听多个Io操作.需要将IO连接(管道)的操作注册上.
     */
    public final Selector selector;

    /**
     * 服务端Socket管道
     */
    public final ServerSocketChannel serverSocketChannel;


    public Reactor(int port)throws Exception{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost(),port);
        serverSocketChannel.socket().bind(inetSocketAddress);
        serverSocketChannel.configureBlocking(false);
        //向Selector的多路复用器注册该IO连接的OP_ACCEPET操作
        SelectionKey selectionKey =  serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //添加处理器
        selectionKey.attach(null);
    }



    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                  //调用操作系统内核IO多路复用的函数,开始监听注册到该selector上的IO操作,如果有任意一个IO操作就绪,则返回.否则当前线程阻塞到该系统调用上
                  selector.select();
                  Set<SelectionKey>  keys = selector.selectedKeys();
                  Iterator<SelectionKey> it = keys.iterator();
                  while(it.hasNext()){
                       SelectionKey key =  it.next();
                       dispatch(key);
                  }
                  keys.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    void dispatch(SelectionKey key){
        Runnable r =  (Runnable)key.attachment();
        r.run();
    }
}
