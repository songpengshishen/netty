package com.wsp.netty.reactor;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 事件接受者,处理IO连接的Accept
 * @author wsp
 * @since 2018/01/09
 */
public class Acceptor implements Runnable{



    protected final Selector selector;
    protected final ServerSocketChannel serverChannel;

    public Acceptor(Selector selector, ServerSocketChannel serverChannel){
        this.selector = selector;
        this.serverChannel = serverChannel;
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
            SocketChannel clientChannel = serverChannel.accept();
            if(null != clientChannel){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
