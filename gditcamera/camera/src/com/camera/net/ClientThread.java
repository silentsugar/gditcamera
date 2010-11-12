package com.camera.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;
import android.util.Log;

import com.camera.vo.DataHead;

/**
 * 客户端线程，开启该线程上传文件
 * @author tian
 *
 */
public class ClientThread extends Thread { 
	
	private Socket clientSocket;
	private Handler handler;
	private InputStream fromServer;
	private OutputStream toServer;
	private DataHead dataHead;
	private DataInputStream dataIn;
	private InputStreamReader dataFromServer;
	
	/**
	 * 构造函数
	 * @param socket 与服务端连接的socket
	 * @param handler 返回信息给界面
	 * @param dataHead 每个数据包的包头
	 * @param dataIn 所上传的文件的数据流
	 */
	public ClientThread(Socket socket,Handler handler,DataHead dataHead,DataInputStream dataIn){
		this.clientSocket = socket;
		this.handler = handler;
		this.dataHead = dataHead;
		this.dataIn = dataIn;
	}
	
	@Override
	public void run() {
			try {
			toServer = clientSocket.getOutputStream();
			fromServer = clientSocket.getInputStream();
			String data = null;
			String result = "";
			boolean finished = false;
			boolean posted = false;
			
			//向服务端传输数据
			new SenderThread() {
				
				@Override
				public void action() {
//					try {
//						toServer.write("[Client=>Server]".getBytes("GB2312"));
//						toServer.flush();
//						Thread.sleep(1000);
//					} catch (IOException e) {
//						e.printStackTrace();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
				@Override
				public void run(){
					try {
						byte [] byDatahead = DataHeadUtil.dataHead2Byte(dataHead);
						toServer.write(byDatahead);
//						for(int i=1;i<10;i++)
							toServer.write(new byte[]{1,13});
						Log.e("=>finished bytes:",byDatahead.length+"Bytes");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			
			//获取服务器回应信息
			new RecipientThread(){
				byte [] b = new byte[1024];
				int len = -1;
				
				@Override
				public void action() {
					try {
						len=fromServer.read(b);
						for(int i=0;i<len;i++){
							System.out.printf("b["+i+"]=0x%x", b[i]);
							Log.e("b["+i+"]=", Integer.toHexString((int)b[i]));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}