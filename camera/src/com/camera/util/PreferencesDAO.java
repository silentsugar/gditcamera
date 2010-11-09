package com.camera.util;

import android.content.Context;

import com.camera.vo.Preferences;

/**
 * 配置信息实体类的数据访问对象
 * @author yaotian
 *
 */
public class PreferencesDAO {

	private Context context;
	
	public PreferencesDAO(Context c){
		this.context = c;
	}

	public boolean save(Preferences p){
		return false;
	}
	
	public boolean saveByKey(String key,String value){
		return false;
	}

	public boolean delete(Preferences p){
		return false;
	}
	
	public boolean deleteByKey(String key,String value){
		return false;
	}
	
	public boolean update(Preferences p){
		return false;
	}
	
	public boolean updateByKey(String key,String value){
		return false;
	}
}
