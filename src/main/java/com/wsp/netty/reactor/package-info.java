/**
 * 根据Scalable IO in Java写的 Reactor反应堆IO模式 的实例。
 *
 *
 *  一个channel可以注册到多个Selector多路复用器上
 *  每将一个channel注册到一个Selector上会产生一个该管道与该Selector的唯一关联标识SelectionKey,与感兴趣的事件无关.可以通过改变这个key的值来动态修改channel的
 *  状态和感兴趣事件
 *  select调用是系统调用会触发操作系统内核函数来处理多路复用.select和epoll还需了解
 */
package com.wsp.netty.reactor;