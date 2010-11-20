package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

/**
 * 图片压缩灯
 * @author 郑澍璋
 */
public class ImageCompress {

	/**压缩目标图像宽度大小*/
	public static int maxWidth = 400;
	/**压缩目标图像高度大小*/
	public static int maxHeight = 400;
	
	/** 图像质量*/
	private static int quality = 80;
	
	public ImageCompress() {
		
	}
	
	public static String compress(String filePath) throws Exception {
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
		//计算缩放后的图片大小
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
		newOpts.inSampleSize = (int) ratio + 1;
		//inJustDecodeBounds设为false表示把图片读进内存中
		newOpts.inJustDecodeBounds = false;
		//设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
		newOpts.outHeight = destHeight;
		newOpts.outWidth = destWidth;
		destBitmap = BitmapFactory.decodeFile(filePath, newOpts);
		if(destBitmap == null) {
			throw new Exception("Can not compress the image file!!");
		}
		destFilePath = "/mnt/sdcard/" + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, quality, os);
		os.close();
		return destFilePath;
	}
}
