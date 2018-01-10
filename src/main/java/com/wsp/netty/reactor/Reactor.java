package com.wsp.netty.reactor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * reactor反应堆IO设计模式,Reactor负责EventLoop,将就绪的IO事件分发给具体的Handler执行.
 *
 * 分为俩种Reactor:
 *
 *  1.MainReactor:监听IO连接(TCP的三次握手)
 *
 *  2.SubReactor:监听IO的各种操作(读/写)
 *
 * @author wsp
 * @since 2018/01/09
 */
public abstract class Reactor extends Thread{

    /**
     * java Nio核心类,通过封装操作系统IO多路复用模型的select epoll poll系统调用来实现监听多个Io连接的多种操作.需要将IO连接(管道)的操作注册上.
     */
    protected Selector selector;

    /**
     * 服务端Socket管道
     */
    protected final ServerSocketChannel serverSocketChannel;

    protected final int port;
    protected final boolean isMainReactor;/*是否是主Reactor*/
    protected final boolean useMultipleReactors;/*是否使用多Reactor*/
    protected final long timeOut;
    private static final long DEFAULT_TIMEOUT = 3000;

    public Reactor(ServerSocketChannel serverChannel,int port)throws Exception{
        this(port,false,false,DEFAULT_TIMEOUT,serverChannel);
    }

    public Reactor(ServerSocketChannel serverChannel,int port,long timeOut)throws Exception{
       this(port,false,false,timeOut,serverChannel);
    }


    public Reactor(int port,boolean isMainReactor,boolean useMultipleReactors,long timeOut,ServerSocketChannel serverChannel)throws Exception{
        this.port = port;
        this.isMainReactor = isMainReactor;
        this.useMultipleReactors = useMultipleReactors;
        this.timeOut = timeOut;
        this.serverSocketChannel = serverChannel;
        init();
    }




    private void init()throws Exception{
        selector = Selector.open();
        if(isMainReactor){
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            key.attach(newAcceptor(selector));
        }
    }


    public abstract Acceptor newAcceptor(Selector selector);



    @Override
    public void run() {
        try {
            /**EventLoop**/
            while(!Thread.interrupted()){
                  //调用操作系统内核IO多路复用的函数,开始监听注册到该selector上的IO操作,如果有任意一个IO操作就绪,则返回.否则当前线程阻塞到该系统调用上
                  if(selector.select(timeOut)>0){
                      Set<SelectionKey>  keys = selector.selectedKeys();
                      Iterator<SelectionKey> it = keys.iterator();
                      while(it.hasNext()){
                          SelectionKey key =  it.next();
                          dispatch(key);
                          it.remove();
                      }
                  }
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
