package com.camera.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ClientThread extends Thread {

	private static final String tag = "ServerActivity";
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 9999;
	
	private Socket clientSocket;
	private Handler handler;
	
	public ClientThread(Handler handler){
		this.handler = handler;
	}
	
	@Override
	public void run() {
		try {
			Log.d(tag, "try to connet server: "+HOST);
			clientSocket = new Socket(HOST, PORT);
			Bundle dataBundle = new Bundle();
			dataBundle.putString("result", "正在发送文件..");
			Message m = new Message();
			m.setData(dataBundle);
			handler.sendMessage(m);

			DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
			InputStream fromSDCard = FileUtil.file2Stream("/sdcard/8.png");
			byte [] buf = new byte[100];
			int len = -1;
			while((len = fromSDCard.read(buf))!=-1){
				toServer.write(buf, 0, len);
			}
			fromSDCard.close();
			
			//等待服务器信息
			DataInputStream fromServer = new DataInputStream(clientSocket.getInputStream());
//			ByteArrayInputStream bis = new ByteArrayInputStream(buf);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			String respone = "";
			while(true){
				if((len=fromServer.read(buf))!=-1){
					Log.d("formServer","");
					bos.write(buf, 0, len);
				}else{
					Log.d("toClient","");
//					m = new Message();
//					dataBundle.putString("result", respone);
//					m.setData(dataBundle);
					bos.writeTo(System.out);
					toServer.close();
				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
