package com.camera.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

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
			file.mkdir();
		}
		file = new File(Constant.PIECE_FOLDER);
		if(!file.exists()) {
			file.mkdir();
		}
		cutFile(context);
		return true;
	}
	
	public static void cutFile(Context context) {
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camera/wUpload/a.txt";
		System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camera/wUpload/a.txt");
		try {
			CutFileUtil cutFileUtil = new CutFileUtil(context, filePath);
			byte[] buf = new byte[CutFileUtil.pieceSize];
			//System.out.println("--------------------开始获取切片--------------------------");
			while(cutFileUtil.getNextPiece(buf) != -1) {
//				System.out.println("--------------------获取下一个切片--------------------------");
//				String str = new String(buf, "GB2312");
//				System.out.println(str);
			}
			//System.out.println("--------------------获取完所有切片--------------------------");
		} catch (Exception e) {
			Toast.makeText(context, "文件切错了！！", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
	}
	
}
