package com.camera.vo;

/**
 * 第一部分--描述信息
 * @author tian
 *
 */
public class DataDesc {

	/**起始段*/
	private String startId;
	/**设备编号*/
	private int deviceCode;
	/**口令*/
	private int command;
	/**时间格式：年-月-日x时:分:秒*/
	private String time;
	/**单位名*/
	private String unitName;
	/**测站名*/
	private String surveyStation;
	/**描述*/
	private String desc;
	/**结束*/
	private String endId;
	
	public String getStartId() {
		return startId;
	}
	public String setStartId(String startId) {
		this.startId = startId;
		return "error";
	}
	public int getDeviceCode() {
		return deviceCode;
	}
	public String setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
		return "error";
	}
	public int getCommand() {
		return command;
	}
	public String setCommand(int command) {
		this.command = command;
		return "error";
	}
	public String getTime() {
		return time;
	}
	public String setTime(String time) {
		this.time = time;
		return "error";
	}
	public String getUnitName() {
		return unitName;
	}
	public String setUnitName(String unitName) {
		this.unitName = unitName;
		return "error";
	}
	public String getSurveyStation() {
		return surveyStation;
	}
	public String setSurveyStation(String surveyStation) {
		this.surveyStation = surveyStation;
		return "error";
	}
	public String getDesc() {
		return desc;
	}
	public String setDesc(String desc) {
		this.desc = desc;
		return "error";
	}
	public String getEndId() {
		return endId;
	}
	public String setEndId(String endId) {
		this.endId = endId;
		return "error";
	}
	
	@Override
	public String toString(){
		return "";
	}
	
	/**
	 * 把该对象转换成字节数组返回
	 * @return
	 */
	public byte [] toBytes(){
		return new byte[0];
	}
}
