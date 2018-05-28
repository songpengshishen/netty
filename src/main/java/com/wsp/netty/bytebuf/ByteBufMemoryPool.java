package com.wsp.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * netty中字节内存池的使用
 *
 * @author wsp
 */
public class ByteBufMemoryPool {

    public static final String dataStr = "Every man is his own worst enemy.";
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        unByteBufPool();
        System.out.println("当前执行时间 : " + (System.currentTimeMillis()-startTime));
    }


    public static void unByteBufPool() {
        for (int i = 0; i < 10000; i++) {
            ByteBuf heapByteBuf = Unpooled.buffer(1024*1024);
            heapByteBuf.writeBytes(dataStr.getBytes());
            if(heapByteBuf.release()){
                heapByteBuf = null;
            }
        }
    }

    public static void byteBufPool() {
        for (int i = 0; i < 10000; i++) {
            ByteBuf heapByteBuf = PooledByteBufAllocator.DEFAULT.buffer(1024*1024);
            heapByteBuf.writeBytes(dataStr.getBytes());
            heapByteBuf.release();
        }
    }


}
