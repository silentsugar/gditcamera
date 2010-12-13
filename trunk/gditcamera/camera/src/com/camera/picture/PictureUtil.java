package com.camera.picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.camera.util.StringUtil;
import com.camera.vo.Constant;

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
	private String[] imageSuffixs;
	
	
	
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
		//提取文件格式数组
		String imageSuffix = Constant.IMAGE_SUFFIX;
		imageSuffixs = imageSuffix.split(";");
		for(File file : files) {
			if(file.isDirectory()) {
				continue;
			}
			filePaths.add(file.getAbsolutePath());
		}
		return filePaths;
	}
	
	/**
	 * 检测文件是否是图片资源
	 * @param file 文件
	 * @return 是或否
	 */
	public boolean isImage(File file) {
		String fileName = file.getName();
		int point = fileName.lastIndexOf(".");
		if(point < 0) {
			return false;
		}
		String suffix = fileName.substring(point + 1);
		System.out.println("suffix : " + suffix);
		for(String str : imageSuffixs) {
			if(suffix.toUpperCase().equals(str)) {
				return true;
			}
		}
		return false;
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
	public Options getImageOptions(String path) {
		if(path == null)
			return null;
		File file = new File(path);
		if(!file.exists())
			return null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		return opts;
	}
	
	public Bitmap getBitmap(String path) {
		if(path == null)
			return null;
		File file = new File(path);
		if(!file.exists())
			return null;
		return BitmapFactory.decodeFile(path);
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
//			Log.d(TAG, "file path : " + path);
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
	public List<String> createThumbnails(Context context, String folderPath) throws Exception {
		List<String> thumbnailPaths = null;
		List<String> paths = getFilePathsFromFolder(folderPath);
		if(paths == null) {
			return thumbnailPaths;
		}
		if(paths.size() > 0)
			thumbnailPaths = new ArrayList<String>();
		for(String filePath : paths) {
			String targetPath = createThumbnail(context, filePath);
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
	private String createThumbnail(Context context, String filePath) throws Exception {
		//检测文件是否是图片文件
		File file = new File(filePath);
		if(!isImage(file)) {
			// TODO 
			String smallFileName = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(file.getPath());
			String bigFileName = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(file.getPath()) + ".big";
			File smallFile = new File(smallFileName);
			File bigFile = new File(bigFileName);
			InputStream in = context.getAssets().open("file.jpg");
			OutputStream smallOut = new FileOutputStream(smallFile);
			OutputStream bigOut = new FileOutputStream(bigFile);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			smallOut.write(buffer);
			bigOut.write(buffer);
			bigOut.close();
			smallOut.close();
			in.close();
			return smallFileName;
		}
		String thumbnailPath = null;
		Options options = this.getImageOptions(filePath);
		if (options == null) 
			throw new Exception("Can't get the file!");
		//生成缩略图
		thumbnailPath = ImageCompress.extractThumbnail(filePath);
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
	 * 清除切片目录
	 */
	public void clearImagePieces() {
		File folder = new File(Constant.PIECE_FOLDER);
		File[] files = folder.listFiles();
		for(File file : files) {
			file.delete();
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
			mTagetHeight = height;
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
