package com.camera.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.camera.util.ClientThread;
import com.camera.util.ServerThread;
/**
 * 服务端界面
 * @author tian
 *
 */
public class UploadActivity extends Activity implements OnClickListener{
	
	/**UI*/
	private Button btnServerStart;
	private Button btnClientStart;
	private TextView tv1;
	
	/**更新数据的Handler*/
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle dataBundle = msg.getData();
			if(tv1!=null){
				tv1.setText(dataBundle.getString("result"));
			}
		}
	};
	/**客户端访问服务端的线程*/
	private Thread clientThread = new ClientThread(handler);
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_main);

		Log.d("sdcard state", Environment.getExternalStorageState());
		Log.d("sdcard", Environment.getExternalStorageDirectory().getAbsolutePath());

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
			btnServerStart.setClickable(false);
			break;
		case R.id.btnClientStart:
			this.clientThread.start();
			btnClientStart.setClickable(false);
			break;
		}
	}
	private void startServerSocket() {
		new ServerThread().start();
	}

}
