package com.camera.vo;

import android.graphics.drawable.Drawable;

public class FileItem implements Comparable{
	private String fileName;
	private Drawable drawable;
	private String discript;
	public String getDiscript() {
		return discript;
	}
	public void setDiscript(String discript) {
		this.discript = discript;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	@Override
	public int compareTo(Object another) {
		FileItem temp=(FileItem)another;
		return this.fileName.compareTo(temp.getFileName());
	}
	
}
