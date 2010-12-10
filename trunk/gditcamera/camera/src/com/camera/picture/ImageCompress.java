package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.camera.util.StringUtil;
import com.camera.vo.Constant;

/**
 * 图片压缩灯
 * @author 郑澍璋
 */
public class ImageCompress {
	
	public static final String TAG = "ImageCompress";

	/**压缩目标图像宽度大小*/
	public static int mMaxWidth = 700;
	/**压缩目标图像高度大小*/
	public static int mMaxHeight = 700;
	/** 文件大小多少时压缩 */
	public static int mImageCompressSize = Constant.IMAGE_COMPRESS_SIZE;
	
	/** 图像质量*/
	private static final int quality = 80;
	
	public ImageCompress() {
		
	}
	
	public static String compressJPG(String filePath) throws Exception {
		if(!checkIsJPEG(filePath)) {
			return filePath;
		}
		File file = new File(filePath);
		long length = file.length();
		boolean flag = true;
		while(length >= 10) {
			filePath = compress(filePath);
			file = new File(filePath);
			length = file.length();
		}
		Log.i(TAG, "The final image size is:" + length + "; file path is : " + filePath);
		return filePath;
	}
	
	private static boolean checkIsJPEG(String filePath) {
		int index = filePath.lastIndexOf(".");
		final String suffix = filePath.substring(index + 1).toLowerCase();
		if(suffix.equals("jpeg") || suffix.equals("jpg")) {
			Log.d(TAG, "The picture is JPEG suffix, suffix is : " + suffix);
			return true;
		}
		Log.d(TAG, "The picture is not JPEG suffix, suffix is : " + suffix);
		return false;
	}
	
	private static String compress_backup(String filePath) throws Exception {
		String destFilePath = null;
		Bitmap destBitmap;
		int destWidth = 0;
		int destHeight = 0;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		//缩放比例
		double ratio = 0.0;
		int maxWidth = mMaxWidth;
		int maxHeight = mMaxHeight;
		//计算缩放后的图片大小
		if(srcWidth <= mMaxWidth)
			maxWidth = srcWidth;
		if(srcHeight <= mMaxHeight)
			maxHeight = srcHeight;
		if (srcWidth > srcHeight) {
			ratio = srcWidth / maxWidth;
			destWidth = maxWidth;
			destHeight = (int)(srcHeight / ratio);
		} else {
			ratio = srcHeight / maxHeight;
			destHeight = maxHeight;
			destWidth = (int)(srcWidth / ratio);
		}
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
//		newOpts.inSampleSize = (int) ratio + 1;
		//inJustDecodeBounds设为false表示把图片读进内存中
		newOpts.inJustDecodeBounds = false;
		//设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
		newOpts.outHeight = destHeight;
		newOpts.outWidth = destWidth;
		Log.e(TAG, "destHeight:" + destHeight + "; destWidth:" + destWidth);
		newOpts.inSampleSize = calculateInSampleSize2(filePath);
		destBitmap = BitmapFactory.decodeFile(filePath, newOpts);
		if(destBitmap == null) {
			throw new Exception("Can not compress the image file!!");
		}
		destFilePath = Constant.PIECE_COMPRESS_FOLDER + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, quality, os);
		os.close();
		if(!destBitmap.isRecycled()) {
			destBitmap.recycle();
		}
		File file = new File(filePath);
		file.delete();
		return destFilePath;
	}
	

	private static String compress(String filePath) throws Exception {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = 1;
		Bitmap destBitmap = BitmapFactory.decodeFile(filePath, newOpts);
		if(destBitmap == null) {
			throw new Exception("Can not compress the image file!!");
		}
		String destFilePath = Constant.PIECE_COMPRESS_FOLDER + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, 1, os);
		os.close();
		if(!destBitmap.isRecycled()) {
			destBitmap.recycle();
		}
		Log.e(TAG, "File size:" + destFile.length());
		return destFilePath;
	}
	
	/**
	 * 生成缩略图
	 * @param filePath 图片路径
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param quality 图片质量
	 * @param size 大图还是小图，TRUE为大图
	 */
	public static String extractThumbnail(String filePath, int width, 
			int height, int quality, boolean size) throws Exception {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		String thumbnailPath = null;
		int inSampleSize = 0;
		if(size) {
			inSampleSize = calculateInSampleSize(filePath);
			newOpts.inSampleSize = inSampleSize;
			thumbnailPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath) + ".big";
		} else {
			inSampleSize = calculateInSampleSize(filePath) * 2;
			newOpts.inSampleSize = inSampleSize;
			thumbnailPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath);
		}
		newOpts.inJustDecodeBounds = false;
		//设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
		newOpts.outHeight = width;
		newOpts.outWidth = height;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, newOpts);
		Log.i(TAG, "InSampleSize is :" + inSampleSize);
		File bitmapFile = new File(thumbnailPath);
		if (bitmapFile.exists()) {
			bitmapFile.delete();
		}
		FileOutputStream bitmapWtriter = new FileOutputStream(bitmapFile);
		if (!bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		return thumbnailPath;
	}
	
	/**
	 * 生成缩略图时计算读取图片文件要缩小的倍数
	 */
	private static int calculateInSampleSize(String filePath) {
		File file = new File(filePath);
		long fileSize = file.length();
		Log.i(TAG, "File size is :" + fileSize);
		if(fileSize > 4 * 1024 * 1024) {
			return 30;
		} else if(fileSize > 2 * 1024 * 1024) {
			return 22;
		} else if(fileSize > 1024 * 1024) {
			return 15;
		} else if(fileSize > 1024 * 512) { 
			return 8;
		} else if(fileSize > 300 * 1024) {
			return 5;
		} else if(fileSize > 200 * 1024) {
			return 3;
		} else if(fileSize > 100 * 1024) {
			return 2;
		}
		return 1;
	}
	
	/**
	 * 上传图片压缩时计算读取图片文件要缩小的倍数
	 */
	private static int calculateInSampleSize2(String filePath) {
		File file = new File(filePath);
		long fileSize = file.length();
		Log.i(TAG, "File size is :" + fileSize);
		if(fileSize > 4 * 1024 * 1024) {
			return 8;
		} else if(fileSize > 2 * 1024 * 1024) {
			return 6;
		} else if(fileSize > 1024 * 1024) {
			return 4;
		} else if(fileSize > 1024 * 728) { 
			return 2;
		}
		return 1;
	}
}
