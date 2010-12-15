package com.camera.head;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * 第一部分--描述信息(超过1字节的数都是：低字节在前，高字节在后)
 * @author tian
 *
 */
public class DataDesc {
	
	public static final String TAG = "DataDesc";

	/**起始段*/
	private byte[] startId;
	/**设备编号*/
	private byte[] deviceCode;
	/**口令*/
	private byte[] command;
	/**时间格式：年-月-日x时:分:秒*/
	private byte[] time = new byte[7];
	/**单位名*/
	private byte[] unitName;
	/**测站名*/
	private byte[] surveyStation;
	/** 摄像头ID*/
	private byte cameraId;
	/**描述*/
	private byte[] desc;
	/**结束*/
	private byte[] endId;
	
	public DataDesc() {
		startId = new byte[]{'$', 'C', 'A', 'A'};
		endId = new byte[]{'$', 'E', 'N', 'D'};
	}
	
	public String getStartId() {
		return CodeUtil.bytes2String(startId);
	}
	
	public long getDeviceCode() {
		return CodeUtil.bytes2int(deviceCode);
	}
	
	public void setDeviceCode(long deviceCode) {
		if(deviceCode < 0 || deviceCode > 4294967295L) {
			Log.e(TAG, "DeviceCode is out of rang! deviceCode will be set default value 0");
			deviceCode = 0;
		}
		this.deviceCode = CodeUtil.int2bytesMax(deviceCode);
	}
	
	public long getCommand() {
		return CodeUtil.bytes2int(command);
	}
	
	public void setCommand(long command) {
		if(command < 0 || command > 4294967295L) {
			Log.e(TAG, "Command is out of rang! command will be set default value 0");
			command = 0;
		}
		this.command = CodeUtil.int2bytesMax(command);
	}
	
	public String getTime() {
		return null;
	}
	
	public void setTime(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String [] arr = dateFormat.format(date).split("-");
		int year1 = Integer.parseInt(arr[0].substring(0,2));
		int year2 = Integer.parseInt(arr[0].substring(2,4));
		int month = Integer.parseInt(arr[1]);
		int day = Integer.parseInt(arr[2]);
		int hour = Integer.parseInt(arr[3]);
		int minute = Integer.parseInt(arr[4]);
		int second = Integer.parseInt(arr[5]);
		
		byte[] tem;
		int offset = 0;
		tem = CodeUtil.int2bytes(year1);
		time[offset++] = tem[1];
		
		tem = CodeUtil.int2bytes(year2);
		time[offset++] = tem[1];
		
		tem = CodeUtil.int2bytes(month);
		time[offset++] = tem[1];
		
		tem = CodeUtil.int2bytes(day);
		time[offset++] = tem[1];
		
		tem = CodeUtil.int2bytes(hour);
		time[offset++] = tem[1];
		
		tem = CodeUtil.int2bytes(minute);
		time[offset++] = tem[1];
		
		tem = CodeUtil.int2bytes(second);
		time[offset++] = tem[1];
	}
	
	public String getUnitName() {
		try {
			return new String(unitName, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName.getBytes();
	}
	
	public String getSurveyStation() {
		try {
			return new String(surveyStation, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setSurveyStation(String surveyStation) {
		this.surveyStation = surveyStation.getBytes();
	}
	
	public int getCameraId() {
		return cameraId;
	}
	
	public void setCameraId(int cameraId) {
		if(cameraId < 1 || cameraId > 255) {
			Log.e(TAG, "CameraId is out of rang! CameraId will be set default value 0");
			cameraId = 1;
		}
		this.cameraId = CodeUtil.int2bytes(cameraId)[1];
	}
	
	public String getDesc() {
		try {
			return new String(desc, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setDesc(String desc) {
		this.desc = desc.getBytes();
	}
	
	public String getEndId() {
		return CodeUtil.bytes2String(startId);
	}
	
	/**
	 * 把该对象转换成字节数组返回
	 * @return
	 */
	public byte [] getBytes(){
		return new byte[0];
	}
	
	public static void main(String[] args) {
		DataDesc data = new DataDesc();
		data.setDeviceCode(1111111111L);
		data.setCommand(2222222222L);
		data.setDesc("我是郑澍璋");
		data.setSurveyStation("测站");
		data.setCameraId(10);
		data.setTime(new Date());
		data.setUnitName("单位名");
		
		String strPrint = "StartId:" + data.getStartId() + "\n";
		strPrint += "DeviceCide:" + data.getDeviceCode() + "\n";
		strPrint += "Command:" + data.getCommand() + "\n";
		strPrint += "Desc:" + data.getDesc() + "\n";
		strPrint += "SurveyStation:" + data.getSurveyStation() + "\n";
		strPrint += "CameraId:" + data.getCameraId() + "\n";
		strPrint += "UnitName:" + data.getUnitName() + "\n";
		strPrint += "End:" + data.getEndId() + "\n";
		
		System.out.println(strPrint);
	}
	
	public static class Log {
		public static void e(String tag, String info) {
			System.out.println(tag + ":" + info);
		}
	}
}
