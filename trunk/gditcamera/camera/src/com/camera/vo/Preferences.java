package com.camera.vo;

import java.util.Map;

import android.util.Log;

/**
 * 配置信息实体
 * @author yaotian
 *
 */
public class Preferences {

	/**协议号(fixed)*/
	private static final byte [] pho = new byte[]{0x24,0x50,0x48,0x4F};
	/**默认到图片目录*/
	private String defaultImgDir;
	/**分站名称(16byte)*/
	private String subStation;
	/**口令(16byte)*/
	private String command;
	/**测站名称(16byte)*/
	private String surveyStation;
	/**站码(8byte)*/
	private String stationCode;
	/**
	 * 服务器地址列表，
	 * 格式:Map<"192.168.1.1",8080> 或 Map<"www.baidu.com",8080>
	 */
	private Map<String,Integer> hostList;
	
	public String getDefaultImgDir() {
		return defaultImgDir;
	}
	public void setDefaultImgDir(String defaultImgDir) {
		this.defaultImgDir = defaultImgDir;
	}
	public String getSubStation() {
		return subStation;
	}
	public void setSubStation(String subStation) {
		this.subStation = subStation;
	}
	public String getSurveyStation() {
		return surveyStation;
	}
	public void setSurveyStation(String surveyStation) {
		this.surveyStation = surveyStation;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public Map<String,Integer> getHostList() {
		return hostList;
	}
	public void setHostList(Map<String,Integer> hostList) {
		this.hostList = hostList;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * 获取协议号
	 * @return
	 */
	public byte [] getPho(){
		return pho;
	}
	/**
	 * **********************************
	 * 方便地获取主机1和主机2的ip地址和端口
	 * @return
	 * **********************************
	 */
	public String getHost1IP(){
		if(this.hostList==null){
			return null;
		}
		if(this.hostList.size()<=0){
			return null;
		}
		for(String ip : this.hostList.keySet()){
			return ip;
		}
		return null;
	}
	
	public int getHost1Port(){
		if(this.hostList==null){
			return -1;
		}
		if(this.hostList.size()<=0){
			return -1;
		}
		
		for(String ip : this.hostList.keySet()){
			return this.hostList.get(ip);
		}
		return -1;
	}
	
	public String getHost2IP(){
		if(this.hostList==null){
			return null;
		}
		if(this.hostList.size()<=0){
			return null;
		}
		Log.e("Host List Size", this.hostList.keySet().size()+"");
		int i = 1;
		for(String ip : this.hostList.keySet()){
			if(i==2){
				return ip;
			}
			i++;
		}
		return null;
	}
	
	public int getHost2Port(){
		if(this.hostList==null){
			return -1;
		}
		if(this.hostList.size()<=0){
			return -1;
		}
		
		int i = 1;
		for(String ip : this.hostList.keySet()){
			if(i==2)
				return this.hostList.get(ip);
			i++;
		}
		return -1;
	}
	
}
