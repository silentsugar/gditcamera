package com.camera.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.util.ServiceThread.ReturnActionListener;
import com.camera.vo.DataHead;
/**
 * 服务端线程，服务端界面开启
 * @author tian
 *
 */
public class ServerThread extends Thread {

	private static final int LISTEN_PORT = 9999;
	private static final String SERVER_HOST = "127.0.0.1";
	/**服务端Socket*/
	private ServerSocket serverSocket;
	private ServiceThread service;
	private Handler handler;
	/**线程池*/
	private ExecutorService mExecutorService;
	
	public ServerThread(Handler handler){
		if(handler!=null){
			this.handler = handler;
		}
		try {
			serverSocket = new ServerSocket(LISTEN_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mExecutorService = Executors.newCachedThreadPool();
	}
	
	@Override
	public void run() {
		Socket clientSocket = null;
		try{
			while(true){
				Log.d("firstly", "Server listening on port "+LISTEN_PORT);
				clientSocket = serverSocket.accept();
				Log.d("thirdly", "Server received the connection ");
				
				service = new ServiceThread(clientSocket);
				service.setReturnActionListener(new ReturnActionListener() {
					
					@Override
					public void OnReturnAction(String savedPath, DataHead dataHead,
							boolean success) {
//						Message msg = handler.obtainMessage();
//						Bundle toUiData = new Bundle();
//						toUiData.putString("savedPath", savedPath);
//						toUiData.putBoolean("success", success);
//						toUiData.putSerializable("dataHead", dataHead);
//						msg.setData(toUiData);
//						handler.sendMessage(msg);
					}
				});
				mExecutorService.execute(service);
			}
		}catch(IOException e){
			
		}
	}

}
