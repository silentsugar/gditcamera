package com.camera.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.LinearLayout.LayoutParams;

import com.camera.widget.CImageTextView;

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
	    
	    CImageTextView tv = new CImageTextView(this, 0, "上传配置");
	    spec = tabHost.newTabSpec("artists").setIndicator(tv).setContent(R.id.tab_1);
	    tabHost.addTab(spec);
	    
	    CImageTextView tv2 = new CImageTextView(this, 0, "服务器配置");
	    spec = tabHost.newTabSpec("albums").setIndicator(tv2).setContent(R.id.tab_2);
	    tabHost.addTab(spec);
	    tv.setClickedResource();
	    setSpecViewPropertise(tv, tv2);
	    
	    tabHost.setCurrentTab(0);
	}
	
	/**
	 * 调整选项卡的参数
	 * @param v1
	 * @param v2
	 */
	public void setSpecViewPropertise(CImageTextView v1, CImageTextView v2) {
		
		final LinearLayout.LayoutParams lp = new LayoutParams(
                0, -1, 1.0f);
        lp.height = 50;
        v1.setLayoutParams(lp);
        v1.setTextSize(18);
        v1.setBackgroundResource(R.drawable.tab_title_bg_normal);
        v1.setImageSpace(50);
        
        final LinearLayout.LayoutParams lp2 = new LayoutParams(
                0, -1, 1.0f);
        lp2.height = 50;
        v2.setLayoutParams(lp2);
        v2.setTextSize(18);
        v2.setBackgroundResource(R.drawable.tab_title_bg_normal);
        v2.setImageSpace(50);
        
        v1.setShowSelectedBackground(true);
        v2.setShowSelectedBackground(true);
        v1.setClickedBackground(R.drawable.tab_title_bg_press);
        v2.setClickedBackground(R.drawable.tab_title_bg_press);
		List<View> listView = new ArrayList<View>();
		listView.add(v1);
		listView.add(v2);
		v1.setSpecList(listView, R.drawable.tab_title_bg_normal);
		v1.setBackgroundResource(R.drawable.tab_title_bg_press);
		
		v2.setSpecList(listView, R.drawable.tab_title_bg_normal);
		v2.setBackgroundResource(R.drawable.tab_title_bg_normal);
	}
}