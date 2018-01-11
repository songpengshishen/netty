package com.wsp.netty.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 执行对IO感兴趣的读/写事件,本质是一个线程
 * @author wsp
 * @since 2018/01/11
 */
public class Handler implements Runnable{

    final SocketChannel socketChannel;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1000);
    ByteBuffer output = ByteBuffer.allocate(1000);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    public Handler(Selector sel,SocketChannel sc)throws Exception{
        socketChannel = sc;
        socketChannel.configureBlocking(false);
        sk = socketChannel.register(sel,SelectionKey.OP_READ);//注册socket管道读事件到Selector上
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();
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
            if (state == READING) read();
            else if (state == SENDING) send();
        } catch (Exception ex) {  }
    }

    void read() throws IOException {
        socketChannel.read(input);
        process();
        state = SENDING;
        sk.interestOps(SelectionKey.OP_WRITE);
    }

    void send() throws IOException {
        socketChannel.write(output);
        sk.cancel();
    }

    public void process(){

    }

}
