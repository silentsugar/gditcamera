package com.camera.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Environment;
import android.util.Log;

import com.camera.vo.DataHead;
/**
 * 服务线程: 管理所有客户端
 * @author tian
 *
 */
public class ServiceThread implements Runnable { 

	private static final String tag = "ClientThread";
	
	private Socket serverSocket;
	private ReturnActionListener returnActionListener;

	public ServiceThread(Socket socket){
		this.serverSocket = socket;
	}
	
	@Override
	public void run() {
		int finishBytes = 0;
		int uploadSize = -1;
		String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/99.png";
		DataHead dataHead ;
		
		try {
			final InputStream fromClient = this.serverSocket.getInputStream();
			final OutputStream toClient = this.serverSocket.getOutputStream();
			FileOutputStream toSdCard = new FileOutputStream(new File(savePath));

			boolean finished = false;
			boolean readHead = false;
			byte [] b = new byte[122];
			int len = -1;
//			finishBytes+=fromClient.read(dataHeadBytes);

			//等待客户端上传数据，完毕后退出循环
//			while(true){
//				
//				while((len=fromClient.read(b))!=-1){
//					Log.d("fromClient:", new String(b,0,len,"GB2312"));
//				}
//				
//			}
			
			//向客户端发送信息
			new SenderThread(){

				@Override
				public void action() {
					try {
						toClient.write("Msg:[Server=>Client]".getBytes("GB2312"));
//						Log.d("server:", "sending data to Client...");
						Thread.sleep(10000);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}.start();

			//获取客户端传来到信息
			new RecipientThread(){
				byte [] b = new byte[138];
				int len = -1;
				
				@Override
				public void action() {
					try {
						len=fromClient.read(b);
						Log.e("=======Received the data:=====","");
						System.out.println(DataHeadUtil.byte2DataHead(b));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}.start();

//			fromClient.read(b);
//			DataHeadUtil.byte2DataHead(b);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ReturnActionListener getReturnActionListener() {
		return returnActionListener;
	}

	/**
	 * 设置上传完成监听器
	 * @author tian
	 *
	 */
	public void setReturnActionListener(ReturnActionListener returnActionListener) {
		this.returnActionListener = returnActionListener;
	}

	/**
	 * 上传完成监听器
	 * @author tian
	 *
	 */
	public interface ReturnActionListener{
		/**
		 * 上传完成后回调函数
		 * @author tian
		 *
		 */
		public void OnReturnAction(String savedPath,DataHead dataHead,boolean success);
	}
}
