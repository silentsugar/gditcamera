package com.camera.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.camera.util.ServerThread;
/**
 * 服务端界面
 * @author tian
 *
 */
public class ServerActivity extends Activity implements OnClickListener{

	private static final String tag = "ServerActivity";
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 9999;
	
	/**UI*/
	private Button btnServerStart;
	private Button btnClientStart;
	private TextView tv1;
	
	private Socket clientSocket;
	
	/**更新数据的Handler*/
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle dataBundle = msg.getData();
			Log.d(tag, "dataBundle size:"+dataBundle.size()+"");
			if(tv1!=null){
				tv1.setText(dataBundle.getString("result"));
			}
		}
	};
	/**客户端访问服务端的线程*/
	private Thread client2Server = new Thread(){

		@Override
		public void run() {
			try {
				Log.d(tag, "try to connet server: "+HOST);
				clientSocket = new Socket(HOST, PORT);
				Log.d(tag, (clientSocket!=null?"connect success":"connect failed")+"");
				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String result = "";
				String data;
				Bundle dataBundle = new Bundle();
				Message m = handler.obtainMessage();
				while((data=br.readLine())!=null){
					result+=data;
					dataBundle.putString("result", result);
					m.setData(dataBundle);
					Log.d(tag, "data have been send to Handler: "+result);
					handler.sendMessage(m);
				}
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_main);

		btnServerStart = (Button) findViewById(R.id.btnServerStart);
		btnClientStart = (Button) findViewById(R.id.btnClientStart);
		btnServerStart.setOnClickListener(this);
		btnClientStart.setOnClickListener(this);
		tv1 = (TextView) findViewById(R.id.tv1);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnServerStart:
			startServerSocket();
			break;
		case R.id.btnClientStart:
			this.client2Server.start();
			btnClientStart.setClickable(false);
			break;
		}
	}
	private void startServerSocket() {
		new ServerThread().start();
	}

}
