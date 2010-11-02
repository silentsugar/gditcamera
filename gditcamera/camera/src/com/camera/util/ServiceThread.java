package com.camera.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;
/**
 * 管理所有客户端线程，服务端调用
 * @author tian
 *
 */
public class ServiceThread implements Runnable {

	private static final String tag = "ClientThread";
	
	private Socket socket;
	
	private BufferedReader in;
	/**从客户端获取的文件输入流*/
	private InputStream fromClient;
	
	private String msg = "";
	/** 存放客户端socket*/
	private List<Socket> clientList = new ArrayList<Socket>();
	
	public ServiceThread(Socket s){
		this.socket = s;
		this.clientList.add(socket);
	}
	
	@Override
	public void run() {
		try {
			fromClient = socket.getInputStream();
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				FileOutputStream toSDCard = new FileOutputStream(new File("/sdcard/99.png"));
				byte [] buf = new byte[100];
				int len = -1;
				while((len=fromClient.read(buf))!=-1){
					toSDCard.write(buf, 0, len);
				}
				toSDCard.close();
				fromClient.close();
				OutputStream toClient = socket.getOutputStream();
				toClient.write("finish".getBytes());
				socket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMsg() throws IOException{
		PrintWriter pout;
		for(Socket cs : clientList){
			pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cs.getOutputStream())),true);
			Log.d(tag, "send: "+msg);
			pout.println(this.msg);
			this.msg = "";
		}
	}

}
