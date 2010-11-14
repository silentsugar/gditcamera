package com.camera.vo;

import java.util.Map;

/**
 * 配置信息实体
 * @author yaotian
 *
 */
public class Preferences {

	/**默认到图片目录*/
	private String defaultImgDir;
	/**分站名称(16byte)*/
	private String subStation;
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
	
}
