package com.camera.activity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.camera.net.ClientThread;
import com.camera.net.ServerThread;
import com.camera.util.Constant;
import com.camera.vo.DataHead;

/**
 * 上传测试界面
 * 
 * @author tian
 * 
 */
public class UploadActivity extends Activity implements OnClickListener {

	private static final String tag = "ServerActivity";
	private static final String HOST = "112.125.33.161";
	private static final int PORT = 10808;

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
				Log.d("#success", dataBundle.getBoolean("success")+"");
//				tv1.setText((dataBundle.getBoolean("success"))? "上传成功" : "上传失败" );
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
		tv1.setTextColor(Color.BLACK);
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
		new ServerThread(handler).start();
	}

	/**
	 * 传入文件路径，连接服务器，开启客户端上传线程...
	 * @param fileName 文件路径
	 */
	private void startUpload(String fileName) {
		DataHead dataHead = new DataHead();
		
		try {
			Socket s = new Socket(HOST,PORT);
			Log.d("secondly", "client connect to server on "+PORT+" where ip address is "+HOST);
			FileInputStream fromSDcard = new FileInputStream(new File(fileName));
			ClientThread clientThread = new ClientThread(s,handler,dataHead,new DataInputStream(fromSDcard));
			
			byte [] bPho = new byte[]{0x24,0x50,0x48,0x4F};
			dataHead.setPho(bPho);
			dataHead.setSubStation("changzhou");
			dataHead.setSurveyStation("xiaohezhan");
			dataHead.setPhoDesc("一号河流");
			dataHead.setStationCode("12345678");
			dataHead.setCommand("1234567812345678");
			dataHead.setDataTime(new Date());
			dataHead.setCameraId((byte)1);
			dataHead.setCurrentPackage(1);
			dataHead.setDataLength(0);
			
//			int packages = fromSDcard.available()/Constant.PACKAGE_SIZE;
//			if(fromSDcard.available()%Constant.PACKAGE_SIZE!=0){
//				packages++;
//			}
			
			dataHead.setTotalPackage(1);
			//dataHead.setDataLength(fromSDcard.available()+Constant.DATA_HEAD_SIZE);
			
			clientThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
