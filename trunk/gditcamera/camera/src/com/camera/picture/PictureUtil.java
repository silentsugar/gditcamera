package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.camera.util.Constant;
import com.camera.util.StringUtil;

/**
 * 文件操作类
 * @author 郑澍璋
 */
public class PictureUtil {
	
	public static final String TAG = "PictureUtil";

	/** 图片缩略图的宽度*/
	private int thumbnailWidth = 100;
	/** 图片缩略图的高度*/
	private int thumbnailHeight = 100;
	/** 图片缩略图的宽度*/
	private int thumbnailWidth2 = 300;
	/** 图片缩略图的高度*/
	private int thumbnailHeight2 = 300;
	/** 保存要应用上去的缩略图高度*/
	private int mTagetHeight;
	/** 保存要应用上去的缩略图宽度*/
	private int mTagetWidth;
	
	
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
	public List<Bitmap> getBitmapByFolderPath(String folderPath) throws Exception {
		List<String> paths = getFilePathsFromFolder(folderPath);
		return getBitmapList(paths);
	}
	
	/**
	 * 通过路径获取图片资源
	 * @param picturePaths 图片路径列表
	 * @return 如果获取不到任何资源，则返回NULL，否则返回图片资源列表
	 */
	public List<Bitmap> getBitmapList(List<String> picturePaths) {
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
	public Bitmap getBitmap(String path) {
		Bitmap bitmap = null;
		if(path == null)
			return bitmap;
		File file = new File(path);
		if(!file.exists())
			return bitmap;
		bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}
	
	/**
	 * 通过文件夹目录路径，从缩略图目录中获取该目录下的所有缩略图路径
	 * @param folderPath 图片目录
	 * @return 该目录下所有的缩略图路径
	 * @throws Exception
	 */
	public List<String> getThumbnailPathsByFolder(String folderPath) throws Exception {
		List<String> filePaths = null;
		File[] files = getThumbnailFile();
		filePaths = new ArrayList<String>();
		String thumbnailFolderPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(folderPath);
		for(File file : files) {
			String path = file.getAbsolutePath();
			Log.d(TAG, "file path : " + path);
			if(path.contains(thumbnailFolderPath) && !path.contains(".big")) {
				filePaths.add(path);
			}
		}
		return filePaths;
	}
	
	public File[] getThumbnailFile() throws Exception {
		File folder = new File(Constant.THUMBNAIL_FOLDER);
		System.out.println("" + Constant.THUMBNAIL_FOLDER);
		if(!folder.exists() || !folder.isDirectory()) {
			throw new Exception("Cant not find the folder");
		}
		return folder.listFiles();
	}
	
	/**
	 * 创建文件夹的缩略器到指定的缩略图目录
	 * @param folderPath 文件夹目录
	 * @return 缩略图路径列表
	 * @throws Exception
	 */
	public List<String> createThumbnails(String folderPath) throws Exception {
		List<String> thumbnailPaths = null;
		List<String> paths = getFilePathsFromFolder(folderPath);
		if(paths == null) {
			return thumbnailPaths;
		}
		if(paths.size() > 0)
			thumbnailPaths = new ArrayList<String>();
		for(String filePath : paths) {
			String targetPath = createThumbnail(filePath);
			thumbnailPaths.add(targetPath);
		}
		return thumbnailPaths;
		
	}
	
	/**
	 * 创建文件缩略图到指定目录
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private String createThumbnail(String filePath) throws Exception {
		String thumbnailPath = null;
		Bitmap bitmap = this.getBitmap(filePath);
		if (bitmap == null) 
			throw new Exception("Can't get the file!");
		//创建缩略
		this.calculateThumbnailSize(bitmap.getWidth(), bitmap.getHeight(), thumbnailWidth, thumbnailHeight);
		Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, mTagetWidth, mTagetHeight);
		//保存小的缩略图到指定目录
		thumbnailPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath);
		File bitmapFile = new File(thumbnailPath);
		if (bitmapFile.exists()) {
			bitmapFile.delete();
		}
		FileOutputStream bitmapWtriter = new FileOutputStream(bitmapFile);
		if (!bitmap1.compress(Bitmap.CompressFormat.JPEG, 20, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		//保存大的缩略图到指定目录
		this.calculateThumbnailSize(bitmap.getWidth(), bitmap.getHeight(), thumbnailWidth2, thumbnailHeight2);
		Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmap, mTagetWidth, mTagetHeight);
		String thumbnailPath2 = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath) + ".big";
		File bitmapFile2 = new File(thumbnailPath2);
		if (bitmapFile2.exists()) {
			bitmapFile2.delete();
		}
		bitmapWtriter = new FileOutputStream(bitmapFile2);
		if (!bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		return thumbnailPath;
			
	}
	
	/**
	 * 清除指定目录的缩略图
	 * @param folderPath
	 * @throws Exception 
	 */
	public void clearThumbnail(String folderPath) throws Exception {
		String thumbnailFolderPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(folderPath);
		File[] files = getThumbnailFile();
		for(File file : files) {
			String path = file.getAbsolutePath();
			if(path.contains(thumbnailFolderPath)) {
				file.delete();
			}
		}
	}
	
	/**
	 * 计算缩略图的高度和宽度
	 * @param oriWidth 真实图片的宽度
	 * @param oriHeight 真实图片的高度
	 */
	public void calculateThumbnailSize(int oriWidth, int oriHeight, int width, int height) {
		if(oriWidth > oriHeight) {
			mTagetWidth = width;
			mTagetHeight = oriHeight * width / oriWidth;
		} else {
			mTagetWidth = width;
			mTagetWidth = oriWidth * height / oriHeight;
		}
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

}
