package com.camera.activity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

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
import com.camera.vo.DataHead;

/**
 * 服务端界面
 * 
 * @author tian
 * 
 */
public class UploadActivity extends Activity implements OnClickListener {

	private static final String tag = "ServerActivity";
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 9999;

	/** UI */
	private Button btnServerStart;
	private Button btnClientStart;
	private TextView tv1;


	/** 更新数据的Handler */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle dataBundle = msg.getData();
			Log.d(tag, "dataBundle size:" + dataBundle.size() + "");
			if (tv1 != null) {
				tv1.setText(dataBundle.getString("result"));
			}
		}
	};
	/** 客户端访问服务端的线程 */
	private Thread t = new Thread() {

		@Override
		public void run() {
			startUpload(Environment.getExternalStorageDirectory().getAbsolutePath()+"/8.png");
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
		switch (v.getId()) {
		case R.id.btnServerStart:
			startServerSocket();
			btnServerStart.setClickable(false);
			break;
		case R.id.btnClientStart:
			this.t.start();
			btnClientStart.setClickable(false);
			break;
		}
	}

	private void startServerSocket() {
		new ServerThread().start();
	}

	private void startUpload(String fileName) {
		DataHead dataHead = new DataHead();
		dataHead.setPho("apho");
		dataHead.setSubStation("1234567890123456");
		dataHead.setSurveyStation("6543210987654321");
		dataHead.setPhoDesc("三十二个字符，六十四字节。三十二个字符，六十四字节，一二三四五六");
		dataHead.setStationCode("12345678");
		dataHead.setDataTime(new Date());
		dataHead.setCameraId((byte)1);
		dataHead.setCurrentPackage(22);
		dataHead.setTotalPackage(2049);
		dataHead.setDataLength(10000);
		
		try {
			Socket s = new Socket(HOST,PORT);
			FileInputStream fromSDcard = new FileInputStream(new File(fileName));
			ClientThread clientThread = new ClientThread(s,dataHead,new DataInputStream(fromSDcard));
			clientThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
