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
public class Handler implements Invoke{

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
        sel.wakeup();
    }


    @Override
    public void invoke() {
        try {
            if (state == READING) read();
            else if (state == SENDING) send();
        } catch (Exception ex) {  }
    }

    void read() throws IOException {
        int count =  socketChannel.read(input);
        if(count > 0){
            process();
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException {
        System.out.println("发送成功!");
        sk.cancel();
    }

    public void process()throws IOException{
        this.input.flip();
        byte[] b1 = new byte[this.input.limit()];
        this.input.get(b1);
        System.out.println(new String(b1));
        output.put(new String("hello Wsp!").getBytes());
        output.flip();
        socketChannel.write(output);
    }

}
