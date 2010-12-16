package com.camera.head;

/**
 * 第二部分--图像信息(超过1字节的数都是：低字节在前，高字节在后)
 * @author tian
 *
 */
public class DataImage {
	
	public static final String TAG = "DataImage";

	/**该数据头总长度*/
	private static final int LENGTH = 25;
	
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
		for(int i = 0; i < code.length; i ++) {
			System.out.printf("0x%x", code[i]);
			System.out.println();
		}
		return CodeUtil.bytes2int(code);
	}
	public void setCode(long code) {
		if(code < 0 || code > 4294967295L) {
			Log.e(TAG, "DeviceCode is out of rang! deviceCode will be set default value 0");
			code = 0;
		}
		this.code = CodeUtil.reversalBytes(CodeUtil.int2bytesMax(code));
	}
	public byte[] getCommand() {
		return command;
	}
	public void setCommand(byte[] command) {
		this.command = command;
	}
	public byte[] getTime() {
		return time;
	}
	public void setTime(byte[] time) {
		this.time = time;
	}
	public byte[] getCurrentPackage() {
		return currentPackage;
	}
	public void setCurrentPackage(byte[] currentPackage) {
		this.currentPackage = currentPackage;
	}
	public byte[] getTotalPackage() {
		return totalPackage;
	}
	public void setTotalPackage(byte[] totalPackage) {
		this.totalPackage = totalPackage;
	}
	public byte[] getDataLength() {
		return dataLength;
	}
	public void setDataLength(byte[] dataLength) {
		this.dataLength = dataLength;
	}
	
	public static void main(String[] args) {
		DataImage data = new DataImage();
		data.setCode(45001);
		
		String strPrint = "StartId:" + data.getStartId() + "\n";
		strPrint += "Code:" + data.getCode() + "\n";
//		strPrint += "Command:" + data.getCommand() + "\n";
//		strPrint += "Desc:" + data.getDesc() + "\n";
//		strPrint += "SurveyStation:" + data.getSurveyStation() + "\n";
//		strPrint += "CameraId:" + data.getCameraId() + "\n";
//		strPrint += "UnitName:" + data.getUnitName() + "\n";
//		strPrint += "End:" + data.getEndId() + "\n";
		
		System.out.println(strPrint);
	}
	
	public static class Log {
		public static void e(String tag, String info) {
			System.out.println(tag + ":" + info);
		}
	}
	
	
}
