package com.camera.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	private SharedPreferences sp;
    private SharedPreferences.Editor spEdit;
    /**
     * 默认的配置参数实体
     */
    static final Preferences defaultPref;
    static{
		defaultPref = new Preferences();
		Map<String,Integer> hosts = new HashMap<String,Integer>();
		hosts.put("http://112.125.33.161",10808);
		hosts.put("http://192.168.1.2",8080);
		defaultPref.setDefaultImgDir(Constant.SDCARD_PATH + "/DCIM/100MEDIA/");
		defaultPref.setSubStation("changzhou");
		defaultPref.setSurveyStation("xiaohezhan");
		defaultPref.setStationCode("00033001");
    }

	public PreferencesDAO(Context c){
		context = c;
		sp = context.getSharedPreferences("preferences",context.MODE_PRIVATE);
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
			Map<String,Integer> m = new HashMap<String, Integer>();
			String host1Add = sp.getString("host_1",null);
			String host2Add = sp.getString("host_2",null);

			if(host1Add!=null){
				m.put(StringUtil.getIpByHostAdd(host1Add),StringUtil.getPortByHostAdd(host1Add));
			}else{
				return p;
			}
			
			if(host2Add!=null){
				m.put(StringUtil.getIpByHostAdd(host2Add),StringUtil.getPortByHostAdd(host2Add));
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
		}else if(key.equals("host_1")){
			Map<String,Integer> m = defaultPref.getHostList();
			final Set<String> keySet = m.keySet();
			for(final String hostAdd : keySet){
				return hostAdd+m.get(hostAdd);
			}
		}else if(key.equals("host_2")){
			Map<String,Integer> m = defaultPref.getHostList();
			final Set<String> keySet = m.keySet();
			int i=0;
			for(final String hostAdd : keySet){
				if(i++<1)
					continue;
				return hostAdd+m.get(hostAdd);
			}
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
		Map<String,Integer> hostList = p.getHostList();
		if(null!=hostList){
			int i=0;
			for(String hostAdd : hostList.keySet()){
				if(i<1){
					spEdit.putString(Constant.HOST_1, "http://"+hostAdd+":"+hostList.get(hostAdd));
				}else if(i<2){
					spEdit.putString(Constant.HOST_2, "http://"+hostAdd+":"+hostList.get(hostAdd));
				}else{
					spEdit.putString("host_"+i, "http://"+hostAdd+":"+hostList.get(hostAdd));
				}
				i++;
			}
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
