package com.camera.activity;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.camera.util.Constant;
import com.camera.util.IniControl;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;
import com.camera.vo.Preferences;
import com.camera.widget.CEditTextButton;
import com.camera.widget.CTabView;
import com.camera.widget.CTabView.CTabViewFactory;

public class Main extends TabActivity implements OnClickListener {
	
	private final static int REQUESTCODE_FOLDER = 1;//定义转动选择文件的请求代号
	
	private Button mBtnExit;
	private Button mBtnUpdateManager;
	private CEditTextButton btnBrowse;
	private Button mBtnSave;
	/**服务器配置所用到的按钮*/
	private Button mBtnTest1,mBtnTest2,mBtnSave1,mBtnSave2;
	/**所有配置所用到的编辑框*/
	private EditText etSubStation,etCommand,etSurveyStation,
			etStationCode,etHost1Ip,etHost2Ip,etHost1Port,etHost2Port;
	/**所有编辑框，按钮的父容器布局*/
	private RelativeLayout mLayoutSubStation,mLayoutCommand,mLayoutSurveyStation,
			mLayoutStationCode,mLayoutBtnSave,mLayoutBtnSaveTest1,mLayoutBtnSaveTest2;

	/**默认到图片目录*/
	private String defaultImgDir;
	/**分局名称(16byte)*/
	private String subStation;
	/**口令(16byte)*/
	private String command;
	/**测站名称(16byte)*/
	private String surveyStation;
	/**站码(8byte)*/
	private String stationCode;
	private String path;
	/**主机1、2 IP*/
	private String host1Ip,host2Ip;
	/**主机1、2 Port*/
	private String port1,port2;
	
	/**保存配置参数*/
	private PreferencesDAO dao;
	/**标识上传配置编辑框当前是否为可修改状态*/
	private boolean mCanChange1 = false;
	/**标识服务器配置编辑框当前是否为可修改状态*/
	private boolean mCanChangeServ1 = true;
	private boolean mCanChangeServ2 = true;

	/**
	 * 检测服务器的属性
	 */
	private boolean testSuccess = false;
	private ProgressDialog dialog;
	private boolean running = true;
	
	private Handler hander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.getData().getBoolean("success")){
				Toast.makeText(Main.this, "该服务器能正常连接", 300).show();
			}else{
				Toast.makeText(Main.this, "连接失败", 300).show();
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//初始化应用程序
		try {
			IniControl.initConfiguration(this);
			Toast.makeText(this, "初始化出现成功！", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			Toast.makeText(this, "初始化出现异常！", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}

		mLayoutSubStation = (RelativeLayout) findViewById(R.id.layoutSubStation);
		mLayoutCommand = (RelativeLayout) findViewById(R.id.layoutCommand);
		mLayoutSurveyStation = (RelativeLayout) findViewById(R.id.layoutSurveyStation);
		mLayoutStationCode = (RelativeLayout) findViewById(R.id.layoutStationCode);
		mLayoutBtnSave = (RelativeLayout) findViewById(R.id.layoutBtnSave);
		mLayoutBtnSaveTest1 = (RelativeLayout) findViewById(R.id.layoutBtnSaveTest1);
		mLayoutBtnSaveTest2 = (RelativeLayout) findViewById(R.id.layoutBtnSaveTest2);
		
		createTab();
		setListener();
		
		dao = new PreferencesDAO(this);
		initInput();
	}
	
	/**
	 * 把已保存的配置参数填入输入框
	 */
	private void initInput() {
		Preferences savedPref = dao.getPreferences();
		if(savedPref!=null){
			setModifyEnable(false);
			
			btnBrowse.getEditText().setText(savedPref.getDefaultImgDir());
			etSubStation.setText(savedPref.getSubStation());
			etCommand.setText(savedPref.getCommand());
			etSurveyStation.setText(savedPref.getSurveyStation());
			etStationCode.setText(savedPref.getStationCode());
			String ip1 = savedPref.getHost1IP();
			String ip2 = savedPref.getHost2IP();
			int port1 = savedPref.getHost1Port();
			int port2 = savedPref.getHost2Port();
			
			if(ip1!=null){
				etHost1Ip.setText(ip1);
				etHost1Port.setText(port1+"");
				setModifyEnable(false, 1);
			}else{
				mBtnTest1.setEnabled(false);
			}
			if(ip2!=null){
				etHost2Ip.setText(ip2);
				etHost2Port.setText(port2+"");
				setModifyEnable(false, 2);
			}else{
				mBtnTest2.setEnabled(false);
			}
		}else{
			setModifyEnable(true);
		}
	}

	public void setListener() {
		btnBrowse = (CEditTextButton) this.findViewById(R.id.btnBrowse);
		mBtnExit = (Button)this.findViewById(R.id.btnExit);
		mBtnUpdateManager = (Button)this.findViewById(R.id.btnUpdateManager);
//		mBtnTestService = (Button)this.findViewById(R.id.btnTestService);
		mBtnSave = (Button)this.findViewById(R.id.btnSave);
		mBtnSave1 = (Button)this.findViewById(R.id.btnSave1);
		mBtnSave2 = (Button)this.findViewById(R.id.btnSave2);
		mBtnTest1 = (Button)this.findViewById(R.id.btnTest1);
		mBtnTest2 = (Button)this.findViewById(R.id.btnTest2);
		
		/**
		 * 测试功能先不搞...
		 */
		mBtnTest1.setEnabled(false);
		mBtnTest2.setEnabled(false);
		
		
		etSubStation = (EditText) this.findViewById(R.id.etSubStation);
		etCommand = (EditText) this.findViewById(R.id.etCommand);
		etSurveyStation = (EditText) this.findViewById(R.id.etSurveyStation);
		etStationCode = (EditText) this.findViewById(R.id.etStationCode);
		etHost1Ip = (EditText) this.findViewById(R.id.etHost1Ip);
		etHost2Ip = (EditText) this.findViewById(R.id.etHost2Ip);
		etHost1Port = (EditText) this.findViewById(R.id.etHost1Port);
		etHost2Port = (EditText) this.findViewById(R.id.etHost2Port);

		btnBrowse.setOnClickListener(this);
		mBtnSave1.setOnClickListener(this);
		mBtnSave2.setOnClickListener(this);
		mBtnTest1.setOnClickListener(this);
		mBtnTest2.setOnClickListener(this);
		mBtnExit.setOnClickListener(this);
		mBtnUpdateManager.setOnClickListener(this);
//		mBtnTestService.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBrowse:
			Intent intent3 = new Intent();
			intent3.setClass(this, SelectFolderActivity.class);
			this.startActivityForResult(intent3, REQUESTCODE_FOLDER);
			break;
		case R.id.btnUpdateManager:
			Intent intent = new Intent();
			intent.setClass(this, UploadFileActivity.class);
			this.startActivity(intent);
			this.finish();
			break;
		case R.id.btnExit:
			this.finish();
			break;
		case R.id.btnSave:
			if(mCanChange1){
				if(checkUploadConfig()){
					Preferences p = new Preferences();
					p.setDefaultImgDir(defaultImgDir);
					p.setSubStation(subStation);
					p.setCommand(command);
					p.setStationCode(stationCode);
					p.setSurveyStation(surveyStation);
					if(dao.save(p)){
						setModifyEnable(false);
						Toast.makeText(this, "保存成功", 300).show();
					}
				}
			}else{
				setModifyEnable(true);
			}
			break;
		case R.id.btnSave1:
			if(mCanChangeServ1){
				if(checkServerConfig(1)){
					dao.saveByKey(Constant.HOST_1, "http://"+this.host1Ip+":"+this.port1);
					Toast.makeText(this, "服务器1保存成功", 300);
					setModifyEnable(false,1);
				}
			}else{
				setModifyEnable(true, 1);
			}
			break;
		case R.id.btnSave2:
			if(mCanChangeServ2){
				if(checkServerConfig(2)){
					dao.saveByKey(Constant.HOST_2, "http://"+this.host2Ip+":"+this.port2);
					Toast.makeText(this, "服务器2保存成功", 300);
					setModifyEnable(false,2);
				}
			}else{
				setModifyEnable(true, 2);
			}
			break;
		case R.id.btnTest1:
//			if(checkServerConfig(1)){
//				testServer(host1Ip, Integer.parseInt(port1));
//			}
			break;
		case R.id.btnTest2:
//			if(checkServerConfig(2)){
//				testServer(host2Ip, Integer.parseInt(port2));
//			}
			break;
		}
	}
	
	/**
	 * 测试服务器能否连通
	 * @param ip
	 * @param port
	 * @return
	 */
	public void testServer(final String ip, final int port){
		dialog = ProgressDialog.show(this, "","正在连接..", true);
		final Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				int count = 0;
				while(running){
					Log.d("while loop", count+"");
					if(count++>5){
						dialog.dismiss();
						testSuccess = false;
						running = false;
						count = 0;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						dialog.dismiss();
						testSuccess = false;
						running = false;
						count = 0;
						e.printStackTrace();
					} catch (Exception e){
						dialog.dismiss();
						testSuccess = false;
						running = false;
						count = 0;
						e.printStackTrace();
					}
				}
			
			}
		});
		t1.start();
		
		Thread t2 = new Thread(){

			@Override
			public void run() {
				try {
					Log.d("connecting to ", "ip="+ip+"port="+port);
					Socket s = new Socket(ip,port);
					if(s!=null){
						Main.this.running = false;
						dialog.dismiss();
						testSuccess = true;
					}
				} catch (Exception e){
//					e.printStackTrace();
					dialog.dismiss();
					testSuccess = false;
				}

				Message msg = Main.this.hander.obtainMessage();
				Bundle data = new Bundle();
				data.putBoolean("success", testSuccess);
				msg.setData(data);
				Main.this.hander.sendMessage(msg);
			}
			
		};
		t2.start();
	}
	/**
	 * 检查输入参数是否有效
	 */
	private boolean checkUploadConfig() {
		this.defaultImgDir = btnBrowse.getEditText().getText().toString();
		this.subStation = etSubStation.getText().toString();
		this.command = etCommand.getText().toString();
		this.surveyStation = etSurveyStation.getText().toString();
		this.stationCode = etStationCode.getText().toString();
		
		boolean isVaild = true;
		String errMsg = null;
		if((errMsg=StringUtil.isCorrectImgDir(defaultImgDir))!=null){
			isVaild = false;
			EditText et = this.btnBrowse.getEditText();
			et.setError(errMsg);
			this.btnBrowse.removeView(et);
			this.btnBrowse.addView(et);
		}
		if((errMsg=StringUtil.isCorrectSubStation(subStation))!=null){
			isVaild = false;
			this.etSubStation.setError(errMsg);
			mLayoutSubStation.removeView(etSubStation);
			mLayoutSubStation.addView(etSubStation);
		}
		if((errMsg=StringUtil.isCorrectCommand(command))!=null){
			isVaild = false;
			this.etCommand.setError(errMsg);
			mLayoutCommand.removeView(etCommand);
			mLayoutCommand.addView(etCommand);
		}
		if((errMsg=StringUtil.isCorrectSurveyStation(surveyStation))!=null){
			isVaild = false;
			this.etSurveyStation.setError(errMsg);
			mLayoutSurveyStation.removeView(etSurveyStation);
			mLayoutSurveyStation.addView(etSurveyStation);
		}
		if((errMsg=StringUtil.isCorrectStationCode(stationCode))!=null){
			isVaild = false;
			this.etStationCode.setError(errMsg);
			mLayoutStationCode.removeView(etStationCode);
			mLayoutStationCode.addView(etStationCode);
		}
		return isVaild;
	}

	/**
	 * 检查服务器IP 端口有没有填写正确
	 * @param which 1代表检查服务器1，2代表检查服务器2
	 * @return
	 */
	public boolean checkServerConfig(int which){
		boolean isVaild = true;
		switch(which){
		case 1:
			this.host1Ip = etHost1Ip.getText().toString();
			this.port1 = etHost1Port.getText().toString();
			String errMsg = null;
			Log.d("host1Ip", host1Ip+"");
			if((errMsg=StringUtil.isCorrectHostAdd(host1Ip))!=null){
				isVaild = false;
				this.etHost1Ip.setError(errMsg);
				
			}
			if((errMsg=StringUtil.isCorrectPort(port1))!=null){
				isVaild = false;
				this.etHost1Port.setError(errMsg);
				
			}
			return isVaild;
		case 2:
			this.host2Ip = etHost2Ip.getText().toString();
			this.port2 = etHost2Port.getText().toString();
			if((errMsg=StringUtil.isCorrectHostAdd(host2Ip))!=null){
			isVaild = false;
			this.etHost2Ip.setError(errMsg);
			
			}
			if((errMsg=StringUtil.isCorrectPort(port2))!=null){
				isVaild = false;
				this.etHost2Port.setError(errMsg);
				
			}
			return isVaild;
			default :
				return false;
		}
		
	}
	
	/**
	 * 设置按钮上的文字是保存还是修改
	 * @param enabled false时显示“修改”
	 */
	private void setModifyEnable(boolean enabled){
		mCanChange1 = enabled;
		if(enabled){
			mLayoutBtnSave.removeView(mBtnSave);
			mBtnSave.setText("保存");
			mLayoutBtnSave.addView(mBtnSave);
		}else{
			mLayoutBtnSave.removeView(mBtnSave);
			mBtnSave.setText("修改");
			mLayoutBtnSave.addView(mBtnSave);
		}
		
		btnBrowse.getEditText().setEnabled(enabled);
		etSubStation.setEnabled(enabled);
		etCommand.setEnabled(enabled);
		etSurveyStation.setEnabled(enabled);
		etStationCode.setEnabled(enabled);
	}
	
	/**
	 * 设置按钮上的文字是保存还是修改
	 * @param enabled false时显示“修改”
	 * @param which 1代表检查服务器1，2代表检查服务器2
	 */
	private void setModifyEnable(boolean enabled,int which){
		switch(which){
		case 1:
			mCanChangeServ1 = enabled;
			if(enabled){
				mBtnSave1.setText("保存");
				mBtnTest1.setEnabled(true);
			}else{
				mBtnSave1.setText("修改");
			}
			etHost1Ip.setEnabled(enabled);
			etHost1Port.setEnabled(enabled);
			break;
		case 2:
			mCanChangeServ2 = enabled;
			if(enabled){
				mBtnSave2.setText("保存");
				mBtnTest2.setEnabled(true);
			}else{
				mBtnSave2.setText("修改");
			}
			etHost2Ip.setEnabled(enabled);
			etHost2Port.setEnabled(enabled);
			break;
		}
	}
	/**
	 * 创建tab选项卡
	 */
	public void createTab() {
		Resources res = getResources();
	    TabHost tabHost = getTabHost();  
	    TabHost.TabSpec spec;  
	    tabHost.setBackgroundColor(Color.parseColor("#10386B"));
	    
	    CTabViewFactory factory = new CTabViewFactory(this);
	    CTabView tv1 = factory.setText("上传配置").create();
	    CTabView tv2 = factory.setText("服务器配置").create();
	    
	    spec = tabHost.newTabSpec("artists").setIndicator(tv1).setContent(R.id.tab_1);
	    tabHost.addTab(spec);
	    spec = tabHost.newTabSpec("albums").setIndicator(tv2).setContent(R.id.tab_2);
	    tabHost.addTab(spec);
	    tabHost.setCurrentTab(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case REQUESTCODE_FOLDER:
			if(resultCode==Activity.RESULT_OK){
				path=data.getStringExtra("path");
				btnBrowse.getEditText().setText(path);
			}
			break;
		}
	}

}