package com.camera.vo;

import java.util.ArrayList;
import java.util.List;

public class UploadFileList {
	
	private List uploadFileList = new ArrayList();
	
	private List successList = new ArrayList();
	private List failseList = new ArrayList();

	public List getUploadFileList() {
		return uploadFileList;
	}

	public void setUploadFileList(List uploadFileList) {
		this.uploadFileList = uploadFileList;
	}
	
	public Object get(int index) {
		return uploadFileList.get(index);
	}
	
	public void add(Object item) {
		uploadFileList.add(item);
	}
	
	public void remove(Object item) {
		uploadFileList.remove(item);
	}
	
	public void addSuccess(Object item) {
		successList.add(item);
	}
	
	public void addFailse(Object item) {
		failseList.add(item);
	}
	
	public int size() {
		return uploadFileList.size();
	}

}
