package com.suheng.java.thread.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*public class SocketServer {

    private static final String CHARSET_NAME = "UTF-8";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7150);
            Socket socket;
            BufferedReader input;
            PrintWriter output;

            while (true) {
                System.out.println("服务器正在运行，等待客户端连接......");
                socket = serverSocket.accept();
                input = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET_NAME));
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), CHARSET_NAME), true);

                while (true) {
                    String source = input.readLine();
                    if (source != null) {
                        if ("bye".equals(source.toLowerCase())) {
                            System.out.println("客户端请求断开连接");
                            output.println("服务端收到断开指令，已断开连接");
                            break;//跳出循环，return;//跳出方法
                        } else {
                            System.out.println("收到客户端消息：" + source);
                            output.println("ECHO: " + source);
                        }
                    }
                }

                input.close();
                output.close();
                socket.close();
                System.out.println("成功关闭文件操作流与Socket连接");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}*/

/**
 * Socket通信服务端
 */
public class SocketServer implements Runnable {
    private static final String CHARSET_NAME = "UTF-8";
    private Socket socket;

    private SocketServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //用于接收客户端发送过来的消息
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET_NAME));
            //用于向客户端发送消息
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), CHARSET_NAME), true);

            while (true) {
                String source = input.readLine();//读取客户端消息
                if (source != null) {
                    if ("bye".equals(source.toLowerCase())) {
                        output.println("客户端发指令请求断开连接（服务端返回）");
                        break;//跳出循环，return;//跳出方法
                    } else {
                        System.out.println("收到客户端消息：" + source);
                        output.println("ECHO: " + source);//向客户端发送消息
                    }
                }
            }

            output.close();
            input.close();
            socket.close();
            System.out.println("服务端关闭IO操作流与Socket连接！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(7150);
            while (true) {
                System.out.println("服务器正在运行，等待客户端连接......");
                Socket socket = serverSocket.accept();
                /*
                 多线程处理机制：每一个客户端连接之后都启动一个线程，以保证服务器可以同时与多个客户端通信。如果是单线程
                 处理机制，那么服务器每次只能与一个客户端连接，其他客户端无法同时连接服务器，要等待服务器出现空闲才可以连接。
                 */
                cachedThreadPool.execute(new SocketServer(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
