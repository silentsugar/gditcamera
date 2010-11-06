package com.camera.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
			byte [] b = new byte[1024];
			int len = -1;
			String data = null;
			String result = "";
			boolean finished = false;
			boolean posted = false;
			
			//向服务端传输数据，完毕后退出循环
			while(true){
				if(!finished){
					if(!posted){
						//先写入数据头
						toServer.write(DataHeadUtil.dataHead2Byte(this.dataHead));
						while((len = dataIn.read(b))!=-1){
							toServer.write(b, 0, len);
						}
						//再写入数据体
//						while((len = dataIn.read(b))!=-1){
//							toServer.write(b, 0, len);
//						}
						toServer.flush();
						posted = true;
					}else{
						//再获取服务器回应
						dataFromServer = new InputStreamReader(fromServer,"GB2312");
						BufferedReader buf = new BufferedReader(dataFromServer);
						while((data = buf.readLine()) != null){
							result += data;
						}
						Log.d("finish for server...", "OK");
						finished = true;
						//最后返回数据到Ui界面
						Bundle dataBundle = new Bundle();
						Message msg = handler.obtainMessage();
						dataBundle.putString("result", result);
						msg.setData(dataBundle);
						handler.sendMessage(msg);
					}
					
				}else{
					//do other thing
					
				}
				if(result.equals("end")){
					dataIn.close();
					toServer.close();
					clientSocket.close();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}