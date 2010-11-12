package com.camera.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 分片上传数据的数据头
 * @author tian
 *
 */
public class DataHead implements Serializable{

	private static final long serialVersionUID = -5294458703266575872L;
	
	/**id:备用*/
	private String id;
	/**$PHO ASCII码(4byte)*/
	private byte [] pho;
	/**分局名称(16byte)*/
	private String subStation;
	/**测站名称(16byte)*/
	private String surveyStation;
	/**照片描述,Unicode(32char=64byte)*/
	private String phoDesc;
	/**站码(8byte)*/
	private String stationCode;
	/**口令(16byte)*/
	private String command;
	/**时间(7byte)*/
	private Date dataTime;
	/**摄像头ID(1byte)*/
	private byte cameraId;
	/**当前包数(2byte)*/
	private int currentPackage;
	/**总包数,十六进制表示(2byte)*/
	private int totalPackage;
	/**数据帧长度:每个切片大小，不含包头,十六进制表示(2byte)*/
	private int dataLength;
	
	public DataHead(){
		pho = new byte[4];
	}
	
	//=========Getter and Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte [] getPho() {
		return pho;
	}

	public void setPho(byte [] pho) {
		this.pho = pho;
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

	public String getPhoDesc() {
		return phoDesc;
	}

	public void setPhoDesc(String phoDesc) {
		this.phoDesc = phoDesc;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public byte getCameraId() {
		return cameraId;
	}

	public void setCameraId(byte cameraId) {
		this.cameraId = cameraId;
	}

	public int getCurrentPackage() {
		return currentPackage;
	}

	public void setCurrentPackage(int currentPackage) {
		this.currentPackage = currentPackage;
	}

	public int getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(int totalPackage) {
		this.totalPackage = totalPackage;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
