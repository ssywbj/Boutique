package jie.example.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import jie.example.boutique.HandleClientRequestActivity;
import jie.example.utils.FileUtil;
import jie.example.utils.LogUtil;
import android.os.Handler;

/**
 * 服务链接类，用于处理客户端的链接请求
 */
public class ConnectServer implements Runnable {
	private static final String TAG = ConnectServer.class.getSimpleName();
	private ServerSocket mServer;
	private Handler mHandler;
	private int mTempCount = 0;

	public ConnectServer(Handler handler) {
		this.mHandler = handler;
	}

	@Override
	public void run() {
		try {
			mServer = new ServerSocket(0x8910);// 监听端口：0x8910

			while (true) {// 死循环：一直在监听是否有客户端链接进入
				LogUtil.i(TAG, "server is running");
				Socket client = mServer.accept();// 等待客户端的连接：方法执行后服务器将进入阻塞状态，直到客户端连接之后才会向下执行。
				new Thread(new HandleClientRequest(client)).start();// 开启线程处理客户端请求
			}
		} catch (Exception e) {
			LogUtil.e(TAG, TAG + "::run()::" + e.toString());
		}
	}

	public void setTempCount(int tempCount) {
		mTempCount = tempCount;
	}

	/**
	 * 处理客户端请求的类
	 */
	private final class HandleClientRequest implements Runnable {
		private Socket client;
		private InputStream inputStream;

		public HandleClientRequest(Socket socket) {
			this.client = socket;
		}

		@Override
		public void run() {
			try {
				inputStream = client.getInputStream();// 获取客户端数据

				byte[] data = FileUtil.readInputStream(inputStream);// 解析客户端数据
				String info = new String(data, "UTF-8");
				int length = info.length();
				client.close();
				LogUtil.i(TAG, "info = " + info + ", length = " + length);
				mHandler.sendEmptyMessage(HandleClientRequestActivity.MSG_ONE
						+ mTempCount);// 通过Handler发消息到主线程更新界面
				mTempCount++;
			} catch (Exception e) {
				LogUtil.e(TAG, HandleClientRequest.class.getSimpleName()
						+ "::run()::" + e.toString());
			}
		}
	}

	/**
	 * 关闭服务器
	 */
	public void closeServer() {
		try {
			mServer.close();
			mServer = null;
			LogUtil.i(TAG, "server is closed");
		} catch (IOException e) {
			LogUtil.e(TAG, TAG + "::closeServer()::" + e.toString());
		}
	}

}