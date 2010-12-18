package com.camera.head;

import java.util.Date;

import android.content.Context;

import com.camera.util.PreferencesDAO;
import com.camera.vo.Preferences;

public class HeadTool {
	
	private String deviceCode;
	private String command;
	private Date date;
	private String unitName;
	private String surveyStation;
	private int cameraId;
	private String desc;
	private String stationCode;
	private int totalPackage;

	/**
	 * 构造函数
	 * @param context
	 * @param date 时间
	 * @param desc 图片描述
	 * @param totalPackage 图片总包数
	 */
	public HeadTool(Context context, Date date, String desc, int totalPackage) {
		this.desc = desc;
		this.date = date;
		this.totalPackage = totalPackage;
		getConfigValue(context);
	}
	
	public void getConfigValue(Context context) {
		PreferencesDAO dao = new PreferencesDAO(context);
		Preferences vo = dao.getPreferences();
		command = vo.getCommand();
		deviceCode = vo.getCommand();
		unitName = vo.getSubStation();
		surveyStation = vo.getSurveyStation();
		stationCode = vo.getStationCode();
		
		cameraId = 1;
	}
	
	public byte[] getDataDesc() {
		DataDesc data = new DataDesc();
		data.setDeviceCode(deviceCode);
		data.setCommand(command);
		data.setTime(date);
		data.setUnitName(unitName);
		data.setSurveyStation(surveyStation);
		data.setCameraId(cameraId);
		data.setDesc(desc);
		return data.getBytes();
	}
	
	public byte[] getDataImage(int currentPackage, int dataLength) {
		DataImage data = new DataImage();
		data.setCode(Long.parseLong(stationCode));
		data.setCommand(Long.parseLong(command));
		data.setTime(date);
		data.setCurrentPackage(currentPackage);
		data.setTotalPackage(totalPackage);
		data.setDataLength(dataLength);
		return data.getBytes();
	}
	
}
