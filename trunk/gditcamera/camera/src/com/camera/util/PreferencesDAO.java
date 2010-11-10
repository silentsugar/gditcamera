package com.camera.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.camera.vo.Preferences;

/**
 * 配置信息实体类的数据访问对象
 * @author yaotian
 *
 */
public class PreferencesDAO {

	private Context context;
    private SharedPreferences.Editor spEdit;

	public PreferencesDAO(Context c){
		context = c;
		SharedPreferences sp = context.getSharedPreferences("preferences",context.MODE_PRIVATE);
    	spEdit = sp.edit();
	}

	public boolean save(Preferences p){
		spEdit.putString(Constant.IMAGE_DIR, p.getDefaultImgDir());
		spEdit.putString(Constant.STATION_CODE,p.getStationCode());
		spEdit.putString(Constant.STATION_SUB, p.getSubStation());
		spEdit.putString(Constant.STATION_SURVEY, p.getSurveyStation());
		Map<Integer,String> hostList = p.getHostList();
		for(Integer hostId : hostList.keySet()){
			spEdit.putString(Constant.HOST_LIST+hostId.intValue(), hostList.get(hostId));
		}
		return spEdit.commit();
	}
	
	public boolean saveByKey(String key,String value){
		spEdit.putString(key, value);
		return spEdit.commit();
	}

	public boolean delete(Preferences p){
		return spEdit.commit();
	}

	public boolean deleteByKey(String key){
		spEdit.remove(key);
		return spEdit.commit();
	}
	
	public boolean deleteAll(){
		spEdit.clear();
		return spEdit.commit();
	}
	
	
	public boolean update(Preferences p){
		return save(p);
	}
	
	public boolean updateByKey(String key,String value){
		return saveByKey(key, value);
	}
}
