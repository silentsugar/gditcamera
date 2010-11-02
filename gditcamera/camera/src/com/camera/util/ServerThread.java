package com.camera.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;
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
	/**线程池*/
	private ExecutorService mExecutorService;
	
	public ServerThread(){
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
			Log.d("Listening on port:",LISTEN_PORT+"");
			clientSocket = serverSocket.accept();
			Log.d("received a connection,ip: ",clientSocket.getInetAddress()+"");
			mExecutorService.execute(new ServiceThread(clientSocket));
		}catch(IOException e){
			
		}
	}

}
