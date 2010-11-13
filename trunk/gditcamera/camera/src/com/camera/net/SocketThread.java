package com.camera.net;

import java.net.Socket;

import android.os.Handler;

public class SocketThread extends Thread {
	
	private static final String tag = "ServerActivity";
	private static final String HOST = "112.125.33.161";
	private static final int PORT = 10808;
	
	/** 客户端SOCKET对象*/
	private Socket clientSocket;
	/** 界面通信处理对象*/
	private Handler handler;
	

	public SocketThread(Handler handler){
		this.handler = handler;
	}
	
	@Override
	public void run() {
			try {
		
			//向服务端传输数据
			new Thread() {
				
				@Override
				public void run(){
					try {
						//客户端发送数据
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			
			
			//获取服务器回应信息
			new Thread() {
				
				@Override
				public void run(){
					try {
						//接收服务端数据
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
