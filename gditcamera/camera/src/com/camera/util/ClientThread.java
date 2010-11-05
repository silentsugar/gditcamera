package com.camera.util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.camera.vo.DataHead;

/**
 * 客户端线程，开启该线程上传文件
 * @author tian
 *
 */
public class ClientThread extends Thread {
	
	private Socket clientSocket;
	private InputStream fromServer;
	private OutputStream toServer;
	private DataHead dataHead;
	private DataInputStream dataIn;
	
	/**
	 * 构造函数
	 * @param socket 与服务端连接的socket
	 * @param dataHead 每个数据包的包头
	 * @param dataIn 所上传的文件的数据流
	 */
	public ClientThread(Socket socket,DataHead dataHead,DataInputStream dataIn){
		this.clientSocket = socket;
		this.dataHead = dataHead;
		this.dataIn = dataIn;
	}
	
	@Override
	public void run() {
			try {
			fromServer = clientSocket.getInputStream();
			toServer = clientSocket.getOutputStream();
			byte [] b = new byte[1024];
			int len = -1;
			//先写入数据头
			toServer.write(DataHeadUtil.dataHead2Byte(this.dataHead));
			while((len = dataIn.read(b))!=-1){
				toServer.write(b, 0, len);
			}
			dataIn.close();
			toServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}