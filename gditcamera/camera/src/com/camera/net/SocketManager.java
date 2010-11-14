package com.camera.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SocketManager {
	
	public static final String TAG = "SocketManager";
	
	public static final int PACKAGE_SEND_FAIL = -1;
	public static final int PACKAGE_SEND_SUCCESS = 1;
	public static final int TIME_OUT = 0;
	
	/** 服务器接收线程睡眠时间*/
	private static final int RECEIVE_THREAD_SLEEP_TIME = 500;
	
	private static final String tag = "ServerActivity";
	private static final String HOST = "112.125.33.161";
	private static final int PORT = 10808;
	
	/** 客户端SOCKET对象*/
	private Socket socket;
	/** 界面通信处理对象*/
	private Handler handler;
	/** Socket->InputStream对象*/
	private InputStream in;
	/** Socket->OutputStream对象*/
	private OutputStream out;
	/** 要发送给服务器的数据*/
	private byte[] dataBuf;
	/** 发送数据时要读取的BUF长度*/
	private int bufLength = 0;
	private Context context;
	

	public SocketManager(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
		openSocketThread();
		//打开SOCKET套接字
		handler.post(receiveThread);
		receiveThread.start();
	}
	
	/**
	 * 套接字发送线程
	 */
	private Thread sendThread = new Thread() {
		@Override
		public void run(){
			try {
				//客户端发送数据
				out.write(dataBuf, 0, bufLength);
				handler.removeCallbacks(this);
				Log.e(TAG, "hased send the paskage!");
			} catch (Exception e) {
				Log.e(TAG, "failse to send the data to the server!");
//				Toast.makeText(context, "发送数据失败！", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * 套接字接收线程
	 */
	private Thread receiveThread = new Thread() {
		@Override
		public void run(){
			try {
				//服务端数据
				byte[] recDataBuf = new byte[14];
				int length = 0;
				synchronized (this) {
					while(true) {
						if(socket.isClosed()) {
							this.sleep(RECEIVE_THREAD_SLEEP_TIME);
							continue;
						}
						if((length = in.read(recDataBuf)) == -1) {
							this.sleep(RECEIVE_THREAD_SLEEP_TIME);
							Log.v(TAG, "length : " + length);
							continue;
						}
						Log.e(TAG, "length : " + length);
						Message msg = new Message();
						//服务器确定包发送成功
						if(recDataBuf[1] == 0x4F && recDataBuf[2] == 0x4B) {
							msg.what = PACKAGE_SEND_SUCCESS;
							handler.sendMessage(msg);
							
						//服务器确定包发送失败
						} else if(recDataBuf[1] == 0X45 && recDataBuf[2] == 0X52) {
							msg.what = PACKAGE_SEND_FAIL;
							handler.sendMessage(msg);
						}
						//打印返回的数据
						for(int i = 0; i < length; i++){
							Log.e("recDataBuf["+i+"]=", Integer.toHexString((int)recDataBuf[i]));
						}
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "throw exception while receive data from server");
//				Toast.makeText(context, "接收服务端数据出现异常！", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * 打开SOCKET套接字
	 */
	public void openSocketThread() {
		try {
			socket = new Socket(HOST,PORT);
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "file to connect the server");
//			Toast.makeText(context, "连接服务器失败！", Toast.LENGTH_SHORT);
		}
	}
	
	public void send(byte[] dataBuf, int length) {
		//向服务端传输数据
		this.dataBuf = dataBuf;
		this.bufLength = length;
		handler.post(sendThread);
		sendThread.start();
	}

}
