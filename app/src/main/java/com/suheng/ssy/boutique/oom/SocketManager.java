package com.suheng.ssy.boutique.oom;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketManager {
    //private static final String TAG = SocketManager.class.getSimpleName();
    private static final String TAG = "WBJ";
    private static SocketManager sSocketManager = new SocketManager();
    private static final String CHARSET_NAME = "UTF-8";
    private ExecutorService mCachedThreadPool = Executors.newCachedThreadPool();
    private ConnectRunnable mConnectRunnable;

    private SocketManager() {
    }

    static SocketManager getSocketManager() {
        return sSocketManager;
    }

    void connect(String host, int port) {
        mConnectRunnable = new ConnectRunnable(host, port);
        mCachedThreadPool.execute(mConnectRunnable);
    }

    void sendRequest(String request) {
        if (mConnectRunnable != null && mConnectRunnable.isConnected()) {
            mCachedThreadPool.execute(new WorkRunnable(mConnectRunnable.getOutput(), request));
        }
    }

    void disconnect() {
        this.sendRequest("bye");
    }

    private class ConnectRunnable implements Runnable {
        private Socket mSocket;
        private String mHost;
        private int mPort;
        private PrintWriter mOutput;

        ConnectRunnable(String host, int port) {
            mHost = host;
            mPort = port;
        }

        @Override
        public void run() {
            try {
                mSocket = new Socket(mHost, mPort);
                if (mSocket.isConnected()) {
                    Log.d(TAG, "client connect success, remote socket address: " + mSocket
                            .getRemoteSocketAddress() + ", local socket address: " + mSocket.getLocalSocketAddress());

                    BufferedReader input = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), CHARSET_NAME));
                    mOutput = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream(), CHARSET_NAME), true);

                    while (true) {
                        String readLine = input.readLine();
                        if (readLine == null) {
                            break;
                        } else {
                            Log.d(TAG, "server return info: " + readLine);
                        }
                    }

                    input.close();//客户端收到服务端主动关闭连接的响应后，会跳出while循环往下执行，关闭IO操作流和Socket连接
                    mOutput.close();
                    mSocket.close();
                    Log.d(TAG, "client close connect, ip: " + mSocket.getInetAddress() + ", port: " + mSocket.getPort()
                            + ", local ip: " + mSocket.getLocalAddress() + ", local port: " + mSocket.getLocalPort());
                } else {
                    Log.d(TAG, "client connect fail, please try again!");
                }
            } catch (Exception e) {
                Log.e(TAG, "client connect fail: " + e.toString());
            }
        }

        boolean isConnected() {
            return mSocket != null && mSocket.isConnected();
        }

        PrintWriter getOutput() {
            return mOutput;
        }
    }

    private class WorkRunnable implements Runnable {
        private PrintWriter mOutput;
        private String mRequest;

        WorkRunnable(PrintWriter output, String request) {
            mOutput = output;
            mRequest = request;
        }

        @Override
        public void run() {
            if (mOutput != null && mRequest != null) {
                mOutput.println(mRequest);
            }
        }
    }

}
