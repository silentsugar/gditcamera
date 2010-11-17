package com.camera.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.camera.activity.UploadFileActivity;
import com.camera.picture.CutFileUtil;
import com.camera.util.PreferencesDAO;
import com.camera.vo.Preferences;

public class UploadFile {
	
	public static final String TAG = "SocketManager";
	
	//服务器信息
	public static final int PACKAGE_SEND_FAIL = -1;
	public static final int PACKAGE_SEND_SUCCESS = 1;
	public static final int TIME_OUT = 0;
	public static final int FINISH_UPLOAD_FILE = 2;
	public static final int THROW_EXCEPTION = 3;
	public static final int CONNECTION_FAILSE = 4;
	public static final int CONNECTION_SUCCESS = 5;
	
	/** 服务器接收线程睡眠时间*/
	private static final int RECEIVE_THREAD_SLEEP_TIME = 500;
	
	private static final String tag = "ServerActivity";
	private static String HOST = "112.125.33.161";
	private static int PORT = 10808;
	
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
	/** 判断包是否已经发送，如果确认已经发送，再发送下一个包*/
	private int isFinish = 1;
	/** 标识要发送的类型，0为测试服务器，1为发送文件，2为发送多文件*/
	private int sendType = -1;
	
	/** 文件切片工具*/
	private CutFileUtil mCutFileUtil;
	
	private Context context;
	

	public UploadFile(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	/**
	 * 套接字发送线程
	 */
	private Thread sendThread = new Thread() {
		
		/**
		 * 打开SOCKET套接字
		 */
		private void openSocketThread() {
			//获取服务器地址跟端口
			PreferencesDAO preferencesDao = new PreferencesDAO(context);
			Preferences p = preferencesDao.getPreferences();
			HOST = p.getHost1IP();
			PORT = p.getHost1Port();
			

			try {
				System.out.println("start to connect the server!!");
				socket = new Socket(HOST,PORT);
				in = socket.getInputStream();
				out = socket.getOutputStream();
				handler.sendEmptyMessage(CONNECTION_SUCCESS);
			} catch(Exception e) {
				e.printStackTrace();
				Log.e(TAG, "file to connect the server");
				handler.sendEmptyMessage(CONNECTION_FAILSE);
			}
		}
		
		@Override
		public void run(){
			try {
				openSocketThread();
				receiveThread.start();
				//发送数据给服务器
				switch(sendType) {
				//测试服务器是否连接得通
				case 0:
					System.out.println("test server thread run.....");
					break;
				//发送单文件给服务器
				case 1:
					System.out.println("send file thread run.....");
					sendFile();
					break;
				//发送多文件给服务器
				case 2:
					System.out.println("send some file thread run.....");
					break;
				default:
					break;
				}
			} catch (Exception e) {
				Log.e(TAG, "failse to send the data to the server!");
				e.printStackTrace();
				handler.sendEmptyMessage(THROW_EXCEPTION);
			} finally {
				//关闭线程
				System.out.println("stop thread.....");
				this.stop();
				receiveThread.stop();
				sendType = -1;
			}
		}
		
		/**
		 * 测试服务器
		 */
		public void testServer() throws Exception {
			handler.sendEmptyMessage(FINISH_UPLOAD_FILE);
		}
		
		/**
		 * 发送单文件
		 * @throws Exception
		 */
		private void sendFile() throws Exception {
			synchronized (this) {
				byte[] dataBuf = new byte[CutFileUtil.pieceSize];
				int length = 0;
				//从切片对象中一片片获取文件流，上传到服务器
				int i = 0;
				while((length = mCutFileUtil.getNextPiece(dataBuf)) != -1) {
					//如果服务器尚未确认包发送成功，则处于等待状态
					while(isFinish == 0) {
						this.sleep(5);
						continue;
					//服务器确认发送错误
					}
					Log.i(TAG, "send " + ++i + " piece");
					//标识未接收到
					isFinish = 0;
					out.write(dataBuf, 0, length);
					Log.e(TAG, "hased send the paskage!");
				}

				//文件上传完,通知界面已经上传好了一个文件
				handler.sendEmptyMessage(FINISH_UPLOAD_FILE);
				System.out.println("finish send a file to the server!");
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
				synchronized (this) {
					while(true) {
						//服务端数据
						byte[] recDataBuf = new byte[14];
						int length = 0;
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
							//标识包发送成功
							isFinish = 1;
							
						//服务器确定包发送失败
						} else if(recDataBuf[1] == 0X45 && recDataBuf[2] == 0X52) {
							msg.what = PACKAGE_SEND_FAIL;
							handler.sendMessage(msg);
							//标识包发送失败
							isFinish = 2;
						}
						//打印返回的数据
						for(int i = 1; i < 3; i++){
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
	 * 上传文件
	 * @param cutFileUtil 文件切片对象
	 */
	public void upload(CutFileUtil cutFileUtil) {
		this.mCutFileUtil = cutFileUtil;
		sendType = 1;
		handler.post(sendThread);
	}
	
	
	/**
	 * 测试服务器是否连接成功
	 * @param packageHead 包字节流
	 * @param handler
	 */
	public static boolean testServer(String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "file to connect the server");
			return false;
		}
		return true;
	}

}
