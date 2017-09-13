package com.wsp.netty.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单的Bio(block阻塞式IO)网络通信服务端例子
 * @author wsp
 * @since 2017/09/12
 */
public class SimpleBIODemoServer {

    private static final int defaultPort=2101;


    public static void main(String[] args) throws IOException{
        //创建服务端端点对象ServerSocket,监控服务端指定端口
        ServerSocket serverSocket = new ServerSocket(defaultPort);
        //以阻塞的方式去等待接受一个连接,当为单线程模型时,同一个时间段只能处理一个客户端连接.socket为客户端与服务端通信使用
        Socket socket =  serverSocket.accept();
        //使用socket的输入流创建一个缓冲区字符输入流
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //使用socket的输出流创建一个字符输出包装流
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        String requestStr,responseStr;
        while ( (requestStr = in.readLine()) != null){
            if("Done".equalsIgnoreCase(requestStr)){
                break;
            }
            responseStr = processRequest(requestStr);
            out.print(responseStr);
            out.flush();
        }
    }

    private static String processRequest(String request){
        return request;
    }


}
