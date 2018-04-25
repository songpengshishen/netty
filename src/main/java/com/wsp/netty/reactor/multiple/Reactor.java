package com.wsp.netty.reactor.multiple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * 主类
 */
public class Reactor implements Runnable{

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private static final SelectorProvider selectorProvider = SelectorProvider.provider();

    public Reactor(int port) throws IOException
    {
        selector = selectorProvider.openSelector();
        serverSocketChannel = selectorProvider.openServerSocketChannel();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        //每一个SelectionKey代表该管道注册到该selector上的一个感兴趣的事件
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        key.attach(new Acceptor(serverSocketChannel,selector));
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                int n = selector.select();//阻塞在这个系统调用上
                if(n == 0){
                   //如果没有感兴趣的IO事件,则继续调用操作系统轮询select
                    continue;
                }
                Set<SelectionKey> set = selector.selectedKeys();
                Iterator<SelectionKey> iterator = set.iterator();
                while(iterator.hasNext()){
                    dispatch(iterator.next());
                }
                set.clear();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 调度分发感兴趣的key
     * @param k
     */
    private void dispatch(SelectionKey k)
    {
        Runnable runnable = (Runnable) k.attachment();//这里可能是Acceptor也可能是ReadWriteHandler
        if(runnable != null)
            runnable.run();
    }
}
