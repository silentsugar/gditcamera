package com.camera.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

import com.camera.vo.Constant;

/**
 * 应用程序的初始化配置
 * @author Administrator
 */
public class IniControl {

	/**
	 * 初始化应用程序配置 
	 * @param context
	 * @throws IOException
	 */
	public static boolean initConfiguration(Context context) throws IOException {
		File file = new File(Constant.APP_FOLDER);
		if(!file.exists()) {
			file.mkdirs();
		}
		file = new File(Constant.PIECE_FOLDER);
		if(!file.exists()) {
			file.mkdirs();
		}
		file = new File(Constant.THUMBNAIL_FOLDER);
		if(!file.exists()) {
			file.mkdirs();
		}
		file = new File(Constant.DEFAULT_IMAGE_FOLDER);
		if(!file.exists()) {
			file.mkdirs();
		}
		file = new File(Constant.PIECE_COMPRESS_FOLDER);
		if(!file.exists()) {
			file.mkdirs();
		}
		//清空图片压缩目录
		File folder = new File(Constant.PIECE_COMPRESS_FOLDER);
		File[] files = folder.listFiles();
		for(File f : files) {
			f.delete();
		}
		return true;
	}
}
