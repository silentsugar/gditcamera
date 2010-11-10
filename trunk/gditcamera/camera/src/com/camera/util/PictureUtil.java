package com.camera.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 文件操作类
 * @author 郑澍璋
 */
public class PictureUtil {

	/**
	 * 创建文件夹
	 */
	public void createFolder(String fileName) {
		
	}
	
	public void createFile() {
		
	}
	
	public void delete() {
		
	}
	
	/**
	 * 获取目录下面的文件
	 * @param folderPath 目录路径
	 * @return 文件列表
	 */
	public List<String> getFilePathsFromFolder(String folderPath) throws Exception {
		List<String> filePaths = null;
		File folder = new File(folderPath);
		if(!folder.exists() || !folder.isDirectory()) {
			throw new Exception("Cant not find the folder");
		}
		File[] files = folder.listFiles();
		if(files.length > 0) 
			filePaths = new ArrayList<String>();
		for(File file : files) {
			if(file.isDirectory()) {
				continue;
			}
			filePaths.add(file.getAbsolutePath());
		}
		return filePaths;
	}
	
	/**
	 * 通过图片文件夹路径获取图片资源
	 * @param folderPath 图片文件夹路径
	 * @return 如果获取不到任何资源，则返回NULL，否则返回图片资源列表
	 */
	public List<Bitmap> getPicturesByFolderPath(String folderPath) throws Exception {
		List<String> paths = getFilePathsFromFolder(folderPath);
		return getPictureThumbnail(paths);
	}
	
	/**
	 * 通过路径获取图片资源
	 * @param picturePaths 图片路径列表
	 * @return 如果获取不到任何资源，则返回NULL，否则返回图片资源列表
	 */
	public List<Bitmap> getPictureThumbnail(List<String> picturePaths) {
		List<Bitmap> bitmaps = null;
		Bitmap bitmap = null;
		if(picturePaths == null || picturePaths.size() < 0) 
			return bitmaps;
		bitmaps = new ArrayList<Bitmap>();
		for(String path : picturePaths) {
			bitmap = BitmapFactory.decodeFile(path);
			if(bitmap == null)
				continue;
			bitmaps.add(bitmap);
		}
		return bitmaps;
	}
	
	/**
	 * 通过路径获取图片的缩略图
	 * @param path 图片资源路径
	 * @return 如果获取不到图片，则返回NULL
	 */
	public Bitmap getPictureThumbnail(String path) {
		Bitmap bitmap = null;
		if(path == null)
			return bitmap;
		File file = new File(path);
		if(!file.exists())
			return bitmap;
		return BitmapFactory.decodeFile(path);
	}

	
}
