package com.camera.vo;

import android.widget.GridView;


public class FileItem implements Comparable<FileItem>{
	public static final int FOLDER = 0;
	public static final int TEXT = 2;
	public static final int IMAGE = 3;
	public static final int AUDIO = 4;
	public static final int VIDEO = 5;
	public static final int PACKAGE = 6;
	public static final int APK = 7;
	public static final int OTHER = 8;
	
	private String title;
	private int imageResid;
	private String size;
	private String position;
	private String lastEditDate;
	private int flag;


	

	public String getSize() {
	
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPosition() {
		return position;
	}

	public String getLastEditDate() {
		return lastEditDate;
	}

	

	

	public void setPosition(String position) {
		this.position = position;
	}

	public void setLastEditDate(String lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageResid() {
		return imageResid;
	}

	public void setImageResid(int imageResid) {
		this.imageResid = imageResid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public int compareTo(FileItem another) {
		return this.title.compareToIgnoreCase(another.getTitle());
	}	

}
