package com.camera.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
/**
 * 服务线程: 管理所有客户端
 * @author tian
 *
 */
public class ServiceThread implements Runnable {

	private static final String tag = "ClientThread";
	
	private Socket socket;

	public ServiceThread(Socket s){
		this.socket = s;
	}
	
	@Override
	public void run() {
		try {
			InputStream fromClient = this.socket.getInputStream();
			FileOutputStream toSdCard = new FileOutputStream(new File("/mnt/sdcard/99.png"));
			byte [] dataHead = new byte[122];
			fromClient.read(dataHead);
			DataHeadUtil.byte2DataHead(dataHead);
			//先读取头部数据
			byte [] b = new byte[1024];
			int len = -1;
			while((len=fromClient.read(b))!=-1){
				toSdCard.write(b, 0, len);
			}
			toSdCard.close();
			socket.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
