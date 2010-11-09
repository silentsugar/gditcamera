package com.camera.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;


public class CutFileUtil {
	
	/** 切片的大小*/
	public static final int pieceSize = 2000;
	/** 包头信息*/
	private static byte[] packageHead;
	
	/** 文件名*/
	private String filePath;
	
	/** 文件路径*/
	private File file;
	
	/** 当前切片数量*/
	private int pieceNum = 1;
	
	/** 文件大小,单位为byte*/
	private int fileSize;
	
	/** 当前获取的文件切片坐标*/
	private int nCurrentPiece = 0;
	
	/** 文件切片名集合*/
	private List<String> pieceFiles;
	
	/** Context对象*/
	private Context context;
	
	public CutFileUtil(Context context, String filePath) throws Exception {
		this.context = context;
		this.filePath = filePath;
		file = new File(filePath);
		if(!file.exists()) {
			throw new Exception("File not exist!");
		}
		pieceFiles = new ArrayList<String>();
		cutFile();
	}
	
	/**
	 * 文件切片
	 */
	private void cutFile() {
		try {
			InputStream in = context.openFileInput(filePath);
			fileSize = in.available();
			byte[] buf = new byte[pieceSize];
			while(true) {
				if(in.read(buf, 0, pieceSize) > 0) {
					packagePiece(buf);
					pieceNum ++ ;
				} else {
					packagePiece(buf);
					break;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 组装切片流
	 * @throws IOException 
	 */
	private void packagePiece(byte[] buf) throws IOException {
		String pieceName = filePath + pieceNum + ".txt";
		FileOutputStream out = context.openFileOutput(pieceName, Context.MODE_WORLD_WRITEABLE);
		pieceFiles.add(pieceName);
		//out.write(packageHead);
		out.write(buf);
		out.close();
	}
	
	/**
	 * 获取切片文件字节流
	 * @param buf 为NULL既可，内部将为buf自动分配内存空间
	 * @return -1表示文件已经读取完,否则返回读取buf的大小
	 * @throws IOException 
	 */
	public int getNextPiece(byte[] buf) throws IOException {
		//如果文件名不存在，说明已经读完，则返回-1
		if(nCurrentPiece >= pieceFiles.size())
			return -1;
		String fileName = pieceFiles.get(nCurrentPiece);
		
		InputStream in = context.openFileInput(fileName);
		int pieceSize = in.available();
		in.read(buf);
		nCurrentPiece ++;
		return pieceSize;
	}
	
	/**
	 * 重新获取切片文件字节流
	 * @param buf 为NULL既可，内部将为buf自动分配内存空间
	 * @return -1表示文件已经读取完,否则返回读取buf的大小
	 * @throws IOException 
	 */
	public int getCurrentPiece(byte[] buf) throws IOException {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//如果文件不存在，说明已经读完，则返回-1
		if(fileName == null)
			return -1;
		InputStream in = context.openFileInput(fileName);
		int pieceSize = in.available();
		in.read(buf);
		return pieceSize;
	}
	
	/**
	 * 删除当前已读取好的文件
	 * @return
	 */
	public boolean removeCurrentFile() {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//如果文件不存在，说明已经读完，则返回-1
		if(fileName == null)
			return false;
		File file = new File(fileName);
		return file.delete();
	}
	
}
