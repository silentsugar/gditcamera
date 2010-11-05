package com.camera.util;

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
	
	private Socket socket;
	private ReturnActionListener returnActionListener;

	public ServiceThread(Socket s){
		this.socket = s;
	}
	
	@Override
	public void run() {
		int finishBytes = 0;
		int uploadSize = -1;
		String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/99.png";
		DataHead dataHead ;
		
		try {
			InputStream fromClient = this.socket.getInputStream();
			uploadSize = fromClient.available();
			Log.d("uploadSize", uploadSize+"");
			FileOutputStream toSdCard = new FileOutputStream(new File(savePath));

			//先读取头部数据
			byte [] dataHeadBytes = new byte[122];
			finishBytes+=fromClient.read(dataHeadBytes);
			dataHead = DataHeadUtil.byte2DataHead(dataHeadBytes);

			//再读取上传文件的数据
			byte [] b = new byte[1024];
			int len = -1;
			while((len=fromClient.read(b))!=-1){
				toSdCard.write(b, 0, len);
				finishBytes+=len;
			}
			Log.d("finishBytes", finishBytes+"");
			
			//判断是否上传成功
			if(returnActionListener!=null){
				returnActionListener.OnReturnAction(savePath, dataHead, (finishBytes==uploadSize ? true : false));
			}
			toSdCard.close();
			socket.close();
			
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
