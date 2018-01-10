package com.wsp.netty.reactor;

/**
 * 事件接受者,处理IO连接的Accept
 * @author wsp
 * @since 2018/01/09
 */
public abstract class Acceptor implements Runnable{







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

    }
}
