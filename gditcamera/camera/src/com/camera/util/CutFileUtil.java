package com.camera.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CutFileUtil {
	
	/** 文件名*/
	private String filePath;
	
	/** 文件路径*/
	private File file;
	
	/** 切片的大小*/
	private int pieceSize = 20;
	
	/** 文件大小,单位为byte*/
	private int fileSize;
	
	/** 文件切片名数组*/
	private String[] pieceFiles;
	
	public CutFileUtil(String filePath) throws Exception {
		file = new File(filePath);
		if(file.exists()) {
			throw new Exception("File not exist!");
		}
		cutFile();
	}
	
	public void cutFile() {
		try {
			//统计文件信息
			calculatorInfo();
			FileOutputStream out = new FileOutputStream(file);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算文件信息
	 * @throws IOException
	 */
	public void calculatorInfo() throws IOException {
		FileInputStream in = new FileInputStream(file);
		fileSize = in.available();
		in.close();
	}
}
