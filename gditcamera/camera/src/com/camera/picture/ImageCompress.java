package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.camera.util.StringUtil;
import com.camera.vo.Constant;

/**
 * ͼƬѹ������
 * @author ֣���
 */
public class ImageCompress {
	
	public static final String TAG = "ImageCompress";

	/**ѹ��Ŀ��ͼ����ȴ�С*/
	public static int mMaxWidth = 700;
	/**ѹ��Ŀ��ͼ��߶ȴ�С*/
	public static int mMaxHeight = 700;
	/** �ļ���С����ʱѹ�� */
	public static int mImageCompressSize = Constant.IMAGE_COMPRESS_SIZE;
	
	/** ͼ������*/
	private static final int quality = 65;
	
	public ImageCompress() {
		
	}
	
	/**
	 * ѹ��ͼƬ����ͼƬ����ָ����Сʱ�Ż����ѹ������ֻ��ѹ��JPGͼƬ
	 * @param filePath ͼƬ·��
	 * @return
	 * @throws Exception �Ҳ����ļ�ʱ�׳��쳣
	 */
	public static String compressJPG(String filePath) throws Exception {
		if(!checkIsJPEG(filePath)) {
			return filePath;
		}
		File file = new File(filePath);
		long length = file.length();
		boolean flag = true;
		while(length >= mImageCompressSize) {
			filePath = compress(filePath);
			file = new File(filePath);
			length = file.length();
		}
		Log.i(TAG, "The final image size is:" + length + "; file path is : " + filePath);
		return filePath;
	}
	
	/**
	 * ����Ƿ���JPEGͼƬ
	 * @param filePath ͼƬ·��
	 * @return
	 */
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
		//���ű���
		double ratio = 0.0;
		int maxWidth = mMaxWidth;
		int maxHeight = mMaxHeight;
		//�������ź��ͼƬ��С
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
		//���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ�Ŀǰ��ֻ����ֻ��ͨ��inSampleSize���������ţ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ
//		newOpts.inSampleSize = (int) ratio + 1;
		//inJustDecodeBounds��Ϊfalse��ʾ��ͼƬ�����ڴ���
		newOpts.inJustDecodeBounds = false;
		//���ô�С�����һ���ǲ�׼ȷ�ģ�����inSampleSize��Ϊ׼���������������ȴ��������
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
		newOpts.inSampleSize = calculateInSampleSize2(filePath);
		Bitmap destBitmap = BitmapFactory.decodeFile(filePath, newOpts);
		if(destBitmap == null) {
			throw new Exception("Can not compress the image file!!");
		}
		String destFilePath = Constant.PIECE_COMPRESS_FOLDER + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, quality, os);
		os.close();
		if(!destBitmap.isRecycled()) {
			destBitmap.recycle();
		}
		Log.e(TAG, "File size:" + destFile.length());
		return destFilePath;
	}
	
	/**
	 * ��������ͼ
	 * @param filePath ͼƬ·��
	 * @param size ��ͼ����Сͼ��TRUEΪ��ͼ
	 */
	public static String extractThumbnail(String filePath) throws Exception {
		//���ɴ�����ͼ
		Log.i(TAG, "SRC Image Info:");
//		printFileInfo(filePath);
		Options opts = getBitmapOptions(filePath, Constant.THUMBNAIL_BIG_SIZE);
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
		Log.i(TAG, "InSampleSize is :" + opts.inSampleSize);
		String thumbnailPathBig = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath) + ".big";
		File bitmapFile = new File(thumbnailPathBig);
		if (bitmapFile.exists()) {
			bitmapFile.delete();
		}
		FileOutputStream bitmapWtriter = new FileOutputStream(bitmapFile);
		if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		if(!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		Log.i(TAG, "BIG Image Info:");
//		printFileInfo(thumbnailPathBig);
		
		//����С����ͼ
		opts = getBitmapOptions(thumbnailPathBig, Constant.THUMBNAIL_SMALL_SIZE);
		bitmap = BitmapFactory.decodeFile(thumbnailPathBig, opts);
		String thumbnailPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath);
		File bitmapSmallFile = new File(thumbnailPath);
		if (bitmapSmallFile.exists()) {
			bitmapSmallFile.delete();
		}
		bitmapWtriter = new FileOutputStream(bitmapSmallFile);
		if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		if(!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		Log.i(TAG, "SMALL Image Info:");
//		printFileInfo(thumbnailPath);
		return thumbnailPath;
	}
	
	public static void printFileInfo(String filePath) {
		File file = new File(filePath);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		Log.v(TAG, "Image file size is: " + file.length() 
				+ "; Image size is :" + opts.outWidth + " * " + opts.outHeight);
	}
	
	/**
	 * ��������ͼʱ�����ȡͼƬ�ļ�Ҫ��С�ı���
	 */
	private static Options getBitmapOptions(String filePath, int imageSize) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		//���ű���
		int ratio = 0;
		if(srcWidth > srcHeight) {
			int k = srcWidth / imageSize;
			ratio = (srcWidth % imageSize == 0) ? k : k + 1;
		} else {
			int k = srcHeight / imageSize;
			ratio = (srcWidth % imageSize == 0) ? k : k + 1;
		}
		Log.e(TAG, "inSampleSize is :" + ratio);
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = ratio;
		return opts;
	}
	
	/**
	 * �ϴ�ͼƬѹ��ʱ�����ȡͼƬ�ļ�Ҫ��С�ı���
	 */
	private static int calculateInSampleSize2(String filePath) {
		File file = new File(filePath);
		long fileSize = file.length();
		Log.i(TAG, "File size is :" + fileSize);
		return (int)(Math.sqrt((double)fileSize / 100000) + 1);
	}
}