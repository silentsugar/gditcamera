package com.camera.activity;

import java.io.IOException;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.camera.util.IniControl;
import com.camera.util.StringUtil;
import com.camera.widget.CEditTextButton;
import com.camera.widget.CTabView;
import com.camera.widget.CTabView.CTabViewFactory;

public class Main extends TabActivity implements OnClickListener {
	
	private final static int REQUESTCODE_FOLDER = 1;//定义转动选择文件的请求代号
	
	private Button mBtnExit;
	private Button mBtnUpdateManager;
//	private Button mBtnTestService;
	private Button mBtnSave;
	private Button mBtnTest1;
	private Button mBtnTest2;
	private CEditTextButton btnBrowse;
	private EditText etSubStation,etSurveyStation,etStationCode,etHost1Ip,etHost2Ip,etHost1Port,etHost2Port;
	private RelativeLayout mLayoutSubStation,mLayoutSurveyStation,mLayoutStationCode;

	/**默认到图片目录*/
	private String defaultImgDir;
	/**分局名称(16byte)*/
	private String subStation;
	/**测站名称(16byte)*/
	private String surveyStation;
	/**站码(8byte)*/
	private String stationCode;
	private String path;
	
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
		mLayoutSurveyStation = (RelativeLayout) findViewById(R.id.layoutSurveyStation);
		mLayoutStationCode = (RelativeLayout) findViewById(R.id.layoutStationCode);
		
		createTab();
		setListener();
		
//		PreferencesDAO dao = new PreferencesDAO(this);
//		Preferences p = new Preferences();
//		Map<Integer,String> hosts = new HashMap<Integer,String>();
//		hosts.put(1, "http://192.168.1.1:8080");
//		hosts.put(2, "http://192.168.2.1:65534");
//		p.setDefaultImgDir(Environment.getDownloadCacheDirectory().getAbsolutePath());
//		p.setStationCode("12334");
//		p.setSubStation("sub213");
//		p.setSurveyStation("survy233");
//		p.setHostList(hosts);
//		dao.save(p);
	}
	
	public void setListener() {
		btnBrowse = (CEditTextButton) this.findViewById(R.id.btnBrowse);
		mBtnExit = (Button)this.findViewById(R.id.btnExit);
		mBtnUpdateManager = (Button)this.findViewById(R.id.btnUpdateManager);
//		mBtnTestService = (Button)this.findViewById(R.id.btnTestService);
		mBtnSave = (Button)this.findViewById(R.id.btnSave);
		etSubStation = (EditText) this.findViewById(R.id.etSubStation);
		etSurveyStation = (EditText) this.findViewById(R.id.etSurveyStation);
		etStationCode = (EditText) this.findViewById(R.id.etStationCode);
		
		btnBrowse.setOnClickListener(this);
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
//		case R.id.btnTestService:
//			Intent intent2 = new Intent();
//			intent2.setClass(this, SelectFolderActivity.class);
//			this.startActivity(intent2);
//			break;
		case R.id.btnSave:
			checkInput();
			break;
		}
	}
	
	/**
	 * 检查输入参数是否有效
	 */
	private boolean checkInput() {
		this.defaultImgDir = btnBrowse.getEditText().toString();
		this.subStation = etSubStation.getText().toString();
		this.surveyStation = etSurveyStation.getText().toString();
		this.stationCode = etStationCode.getText().toString();
		
		boolean isVaild = true;
		String errMsg = null;
		if((errMsg=StringUtil.isCorrectSubStation(subStation))!=null){
			isVaild = false;
			this.etSubStation.setError(errMsg);
			mLayoutSubStation.removeView(etSubStation);
			mLayoutSubStation.addView(etSubStation);
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
				Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	

}