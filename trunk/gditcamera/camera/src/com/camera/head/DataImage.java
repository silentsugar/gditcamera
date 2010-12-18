package com.camera.head;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;



/**
 * 第二部分--图像信息(超过1字节的数都是：低字节在前，高字节在后)
 * @author 郑澍璋
 *
 */
public class DataImage {
	
	public static final String TAG = "DataImage";

	
	/**图像包识别字符串(ASCII半角)*/
	private byte [] startId;
	/**站号(4字节)*/
	private byte[] code;
	/**口令(4字节)*/
	private byte[] command;
	/**时间(7字节)*/
	private byte[] time = new byte[7];
	/**当前包序号(2字节)*/
	private byte[] currentPackage;
	/**图像包总数(2字节)*/
	private byte[] totalPackage;
	/**图像帧长度(2字节)*/
	private byte[] dataLength;
	
	public DataImage() {
		startId = new byte[]{'$', 'D', 'A', 'A'};
	}
	
	public String getStartId() {
		return CodeUtil.bytes2String(startId);
	}
	
	public long getCode() {
		return CodeUtil.bytes2int(code);
	}
	
	public void setCode(long code) {
		if(code < 0 || code > 4294967295L) {
			Log.e(TAG, "DeviceCode is out of rang! deviceCode will be set default value 0");
			code = 0;
		}
		this.code = CodeUtil.int2bytesMax(code);
	}
	
	public long getCommand() {
		return CodeUtil.bytes2int(command);
	}
	
	public void setCommand(long command) {
		if(command < 0 || command > 4294967295L) {
			Log.e(TAG, "Command is out of rang! deviceCode will be set default value 0");
			command = 0;
		}
		this.command = CodeUtil.int2bytesMax(command);
	}
	
	public byte[] getTime() {
		return time;
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
	
	public long getCurrentPackage() {
		return CodeUtil.bytes2int(currentPackage);
	}
	
	public void setCurrentPackage(int currentPackage) {
		if(currentPackage < 0 || currentPackage > 4294967295L) {
			Log.e(TAG, "CurrentPackage is out of rang! deviceCode will be set default value 0");
			currentPackage = 0;
		}
		this.currentPackage = CodeUtil.reversalBytes(CodeUtil.int2bytes(currentPackage));
	}
	
	public long getTotalPackage() {
		return CodeUtil.bytes2int(totalPackage);
	}
	
	public void setTotalPackage(int totalPackage) {
		if(totalPackage < 0 || totalPackage > 4294967295L) {
			Log.e(TAG, "TotalPackage is out of rang! deviceCode will be set default value 0");
			totalPackage = 0;
		}
		this.totalPackage = CodeUtil.reversalBytes(CodeUtil.int2bytes(totalPackage));
	}
	
	public long getDataLength() {
		return CodeUtil.bytes2int(dataLength);
	}
	
	public void setDataLength(int dataLength) {
		if(dataLength < 0 || dataLength > 4294967295L) {
			Log.e(TAG, "DataLength is out of rang! deviceCode will be set default value 0");
			dataLength = 0;
		}
		this.dataLength = CodeUtil.reversalBytes(CodeUtil.int2bytes(dataLength));
	}
	
	/**
	 * 把该对象转换成字节数组返回
	 * @return
	 */
	public byte [] getBytes(){
		int n = startId.length + code.length + command.length 
			+ time.length + currentPackage.length + totalPackage.length
			+ dataLength.length;
		byte[] headBytes = new byte[n];
		int offset = 0;
		
		offset = addBytes(headBytes, startId, offset);
		offset = addBytes(headBytes, code, offset);
		offset = addBytes(headBytes, command, offset);
		offset = addBytes(headBytes, time, offset);
		offset = addBytes(headBytes, currentPackage, offset);
		offset = addBytes(headBytes, totalPackage, offset);
		offset = addBytes(headBytes, dataLength, offset);
		
		printf(this);
		return headBytes;
	}
	
	private int addBytes(byte[] headBytes, byte[] bytes, int offset) {
		for(int i = 0; i < bytes.length; i ++) {
			headBytes[offset ++] = bytes[i];
		}
		return offset;
	}
	
	public void printf(DataImage data) {
//		DataImage data = new DataImage();
//		data.setCode(45001);
//		data.setCommand(12345);
//		data.setTime(new Date());
//		data.setCurrentPackage(50);
//		data.setTotalPackage(30);
//		data.setDataLength(10000000);
		
		
		String strPrint = "StartId:" + data.getStartId() + "\n";
		strPrint += "Code:" + data.getCode() + "\n";
		strPrint += "Command:" + data.getCommand() + "\n";
		strPrint += "CurrentPackage:" + data.getCurrentPackage() + "\n";
		strPrint += "TotalPackage:" + data.getTotalPackage() + "\n";
		strPrint += "DataLength:" + data.getDataLength() + "\n";
		
		System.out.println(strPrint);
	}

	
}
