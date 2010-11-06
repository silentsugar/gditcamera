package com.camera.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.camera.vo.DataHead;

import android.os.Environment;
import android.util.Log;
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
			InputStream fromClient = this.serverSocket.getInputStream();
			FileOutputStream toSdCard = new FileOutputStream(new File(savePath));

			boolean finished = false;
			boolean readHead = false;
			byte [] b = new byte[122];
			int len = -1;
//			finishBytes+=fromClient.read(dataHeadBytes);

			//等待客户端上传数据，完毕后退出循环
			while(true){
				
				if(!finished){
					//再读取上传文件的数据
					while((len=fromClient.read(b))!=-1){
						//先读取头部数据
						if(!readHead){
							dataHead = DataHeadUtil.byte2DataHead(b);
							uploadSize = dataHead.getDataLength();
							Log.d("uploadSize", uploadSize+"");
							readHead = true;
						}else{
							toSdCard.write(b, 0, len);
//							Log.d("Upload Success !!finishBytes", finishBytes+"");
							DataOutputStream toClient = new DataOutputStream(serverSocket.getOutputStream());
							toClient.write("上传成功".getBytes("GB2312"));
							toClient.flush();
						}
						finishBytes+=len;
					}
				}else{
					//do other thing
					try {
						Thread.sleep(2000);
						Log.d("waitting", ".....");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			

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
