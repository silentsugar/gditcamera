package com.camera.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.camera.vo.Constant;
import com.camera.vo.Preferences;

/**
 * 配置信息实体类的数据访问对象
 * @author yaotian
 *
 */
public class PreferencesDAO {

	private Context context;
	private SharedPreferences sp;
    private SharedPreferences.Editor spEdit;
    /**
     * 默认的配置参数实体
     */
    static final Preferences defaultPref;
    static{
		defaultPref = new Preferences();
		Map<String,String> hosts = new HashMap<String,String>();
//		hosts.put(Constant.HOST_1,"http://112.125.33.161:10808");
//		hosts.put(Constant.HOST_2,"http://1.1.1.1:8080");
		defaultPref.setHostList(hosts);
		defaultPref.setDefaultImgDir(Constant.SDCARD_PATH + "/DCIM/100MEDIA");
		defaultPref.setSubStation("中国水文");
		defaultPref.setSurveyStation("应急监测");
		defaultPref.setStationCode("1090999");
		defaultPref.setCommand("1090999");
    }

	public PreferencesDAO(Context c){
		context = c;
		sp = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
    	spEdit = sp.edit();
	}

	/**
	 * 获取当前配置参数实体
	 * @return
	 */
	public Preferences getPreferences(){
		if(sp.getAll().size()<=0){
			return null;
		}else{
			Preferences p = new Preferences();
			p.setDefaultImgDir(sp.getString(Constant.IMAGE_DIR, ""));
			p.setSubStation(sp.getString(Constant.STATION_SUB, ""));
			p.setCommand(sp.getString(Constant.COMMAND, ""));
			p.setSurveyStation(sp.getString(Constant.STATION_SURVEY, ""));
			p.setStationCode(sp.getString(Constant.STATION_CODE, ""));
			//主机列表
			Map<String,String> m = new HashMap<String, String>();
			String host1Add = sp.getString(Constant.HOST_1,null);
			String host2Add = sp.getString(Constant.HOST_2,null);

			if(host1Add!=null){
				m.put(Constant.HOST_1,host1Add);
			}
			if(host2Add!=null){
				m.put(Constant.HOST_2,host2Add);
			}
			
			p.setHostList(m);
			return p;
		}
	}
	
	/**
	 * 根据字段名获取某项配置参数
	 * @param key
	 * @return
	 */
	public String getPreferencesByKey(String key){
		return sp.getString(key, getDefaultPreferencesByKey(key));
	}
	
	/**
	 * 获取默认的配置参数实体
	 * @return
	 */
	public static final Preferences getDefaultPreferences(){
		return defaultPref;
	}
	
	/**
	 * 根据字段名获取默认的某项配置参数
	 * @param key
	 * @return
	 */
	public static final String getDefaultPreferencesByKey(String key){
		if(key.equals(Constant.IMAGE_DIR)){
			return defaultPref.getDefaultImgDir();
		}else if(key.equals(Constant.STATION_CODE)){
			return defaultPref.getStationCode();
		}else if(key.equals(Constant.STATION_SUB)){
			return defaultPref.getSubStation();
		}else if(key.equals(Constant.STATION_SURVEY)){
			return defaultPref.getSurveyStation();
		}else if(key.equals(Constant.HOST_1)){
			Map<String,String> m = defaultPref.getHostList();
			final Set<String> keySet = m.keySet();
			return m.get(keySet);
		}else if(key.equals(Constant.HOST_2)){
			Map<String,String> m = defaultPref.getHostList();
			final Set<String> keySet = m.keySet();
			return m.get(keySet);
		}
		return null;
	}
	
	/**
	 * 保存配置参数实体
	 * @param p
	 * @return
	 */
	public boolean save(Preferences p){
		spEdit.putString(Constant.IMAGE_DIR, p.getDefaultImgDir());
		spEdit.putString(Constant.STATION_CODE,p.getStationCode());
		spEdit.putString(Constant.STATION_SUB, p.getSubStation());
		spEdit.putString(Constant.COMMAND, p.getCommand());
		spEdit.putString(Constant.STATION_SURVEY, p.getSurveyStation());
		Map<String,String> hostList = p.getHostList();
		if(null!=hostList){
			for(String key : hostList.keySet())
				spEdit.putString(key, hostList.get(key));
		}
		return spEdit.commit();
	}
	
	/**
	 * 根据字段名保存某项配置参数
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveByKey(String key,String value){
		spEdit.putString(key, value);
		return spEdit.commit();
	}


	/**
	 * 根据字段名删除某项配置参数
	 * @param key
	 * @return
	 */
	public boolean deleteByKey(String key){
		spEdit.remove(key);
		return spEdit.commit();
	}
	
	/**
	 * 删除所有配置参数
	 * @return
	 */
	public boolean deleteAll(){
		spEdit.clear();
		return spEdit.commit();
	}
	
	/**
	 * 更新整个配置实体，等同于save(p)
	 * @param p
	 * @return
	 */
	public boolean update(Preferences p){
		return save(p);
	}
	
	/**
	 * 根据字段名更新某项配置参数，等同于saveByKey(key,value)
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean updateByKey(String key,String value){
		return saveByKey(key, value);
	}
}
