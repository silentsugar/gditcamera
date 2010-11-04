package com.camera.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;

import com.camera.widget.CTabView;
import com.camera.widget.CTabView.CTabViewFactory;

public class Main extends TabActivity implements OnClickListener {
	
	private Button mBtnExit;
	private Button mBtnUpdateManager;
	private Button mBtnTestService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		createTab();
		setListener();
	}
	
	public void setListener() {
		mBtnExit = (Button)this.findViewById(R.id.btnExit);
		mBtnUpdateManager = (Button)this.findViewById(R.id.btnUpdateManager);
		mBtnTestService = (Button)this.findViewById(R.id.btnTestService);
		mBtnExit.setOnClickListener(this);
		mBtnUpdateManager.setOnClickListener(this);
		mBtnUpdateManager.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()) {
		case R.id.btnUpdateManager:
			intent.setClass(this, UploadFileActivity.class);
			this.startActivity(intent);
			break;
		case R.id.btnExit:
			this.finish();
			break;
		case R.id.btnTestService:
			intent.setClass(this, SelectFolderActivity.class);
			this.startActivity(intent);
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

}