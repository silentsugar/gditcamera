package com.camera.vo;

import android.graphics.drawable.Drawable;

public class FileItem implements Comparable{
	private String title;
	private int imageResid;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	private String discript;
	public String getDiscript() {
		return discript;
	}
	public void setDiscript(String discript) {
		this.discript = discript;
	}
	
	public int getImageResid() {
		return imageResid;
	}
	public void setImageResid(int imageResid) {
		this.imageResid = imageResid;
	}
	
	@Override
	public int compareTo(Object another) {
		FileItem temp=(FileItem)another;
		return this.title.compareTo(temp.getTitle());
	}
	
}
