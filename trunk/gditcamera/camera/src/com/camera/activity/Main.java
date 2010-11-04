package com.camera.activity;

import android.app.TabActivity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;

import com.camera.widget.CImageTextView;
import com.camera.widget.CTabView;
import com.camera.widget.CTabView.CTabViewFactory;

public class Main extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		createTab();
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