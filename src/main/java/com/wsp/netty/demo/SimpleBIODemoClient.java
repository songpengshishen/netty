package com.wsp.netty.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单的Bio(block阻塞式IO)网络通信客户端例子
 * @author wsp
 * @since 2017/09/12
 */
public class SimpleBIODemoClient {




    public static void main(String[] args) throws IOException{
      Socket socket = new Socket("127.0.0.1",2101);
      PrintWriter out =  new PrintWriter(socket.getOutputStream(),true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()),3);
      out.println("hello socket!");
      String str = in.readLine();
      System.out.println(str);
    }




}
