package com.example.wbj.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Socket通信服务端
 */
public class UdpServer {
    public static void main(String[] args) {
        try {
            System.out.println("UDP 服务端已经启动");

            DatagramSocket socket = new DatagramSocket(7676);
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String ip = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            int length = packet.getLength();
            String msg = new String(buf, 0, length);
            System.out.println("客户端: " + ip + "\tport: " + port + "\t信息: " + msg);

            byte[] receiveMsg = ("长度: " + msg.length()).getBytes();
            DatagramPacket receivePacket = new DatagramPacket(receiveMsg, receiveMsg.length, packet.getAddress(), port);
            socket.send(receivePacket);
            socket.close();

            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
