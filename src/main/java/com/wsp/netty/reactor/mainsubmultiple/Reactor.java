package com.wsp.netty.reactor.mainsubmultiple;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class Reactor extends Thread {

    public static Selector selector;
    private boolean isMain;//主从的标志
    private SelectorProvider selectorProvider = SelectorProvider.provider();


    public Reactor(int port, boolean isMain) throws IOException
    {
        this.isMain = isMain;
        selector = selectorProvider.openSelector();
        System.out.println(selector +" isMainReactor = "+isMain);
        if(isMain){
            ServerSocketChannel serverSocketChannel = selectorProvider.openServerSocketChannel();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            key.attach(new Acceptor(selector, serverSocketChannel));
            selector.wakeup();
            System.out.println(getClass().getSimpleName()+" start on "+port+" ...\n");
        }
    }
    public void run()
    {
        try
        {
            while(!Thread.interrupted()){
                int n = selector.select(10);//会阻塞导致不能register，设置阻塞时间
                if(n == 0)
                    continue;
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    dispatch(iterator.next());
                    iterator.remove();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private void dispatch(SelectionKey k)
    {
        Runnable runnable = (Runnable) k.attachment();
        if(runnable != null)
            runnable.run();
    }
}
