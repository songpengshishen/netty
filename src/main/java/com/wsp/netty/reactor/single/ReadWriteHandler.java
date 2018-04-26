package com.wsp.netty.reactor.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 处理IO读写(非阻塞)
 */
public class ReadWriteHandler implements Runnable {

    private SocketChannel ch;
    private SelectionKey key;
    private Selector selector;
    private ByteBuffer input = ByteBuffer.allocate(1024);
    private ByteBuffer output = ByteBuffer.allocate(1024);
    private static final int READING = 0, SENDING = 1;
    int state = READING;


    public ReadWriteHandler(Selector selector, SocketChannel ch) throws IOException {
        this.ch = ch;
        this.selector = selector;
        ch.configureBlocking(false);
        key = ch.register(selector, 0);
        key.attach(this);
        key.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }


    @Override
    public void run() {
        try {
            if (state == READING)
                read();
            else if (state == SENDING)
                send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean inputIsComplete(){
        return input.hasRemaining();
    }

    boolean outputIsComplete(){
        return output.hasRemaining();
    }

    void read() throws IOException{
        ch.read(input);
        if(inputIsComplete()){
            process();
            state = SENDING;
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException {
        output.flip();
        ch.write(output);
        if(outputIsComplete()){
            key.cancel();
        }
        state = READING;
        key.interestOps(SelectionKey.OP_READ);
    }

    void process(){
        //读数据
        StringBuilder reader = new StringBuilder();
        input.flip();
        while(input.hasRemaining()){
            reader.append((char)input.get());
        }
        System.out.println("[Client-INFO]");
        System.out.println(reader.toString());
        String str = "HTTP/1.1 200 OK\r\nDate: Fir, 10 March 2017 21:20:01 GMT\r\n"+
                "Content-Type: text/html;charset=UTF-8\r\nContent-Length: 23\r\nConnection:close"+
                "\r\n\r\nHelloWorld"+System.currentTimeMillis();
        output.put(str.getBytes());
        System.out.println("process over.... ");
    }

}
