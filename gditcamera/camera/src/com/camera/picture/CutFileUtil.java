package com.camera.picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.camera.activity.UploadFileActivity;
import com.camera.net.DataHeadUtil;
import com.camera.util.StringUtil;
import com.camera.vo.Constant;


public class CutFileUtil {
	
	public static final String TAG = "CutFileUtil";
	
	/** 切片的大小*/
	public static final int pieceSize = 10000;
	/** 包头信息*/
	private static byte[] packageHead;
	
	/** 文件名*/
	private String filePath;
	
	/** 文件路径*/
	private File file;
	
	/** 当前切片数量*/
	private int pieceNum = 1;
	
	/** 切片总数*/
	private int totalPieceNum = 0;
	
	/** 文件大小,单位为byte*/
	private int fileSize;
	
	/** 当前获取的文件切片坐标*/
	private int nCurrentPiece = 0;
	
	/** 文件切片名集合*/
	private List<String> pieceFiles;
	
	/** 标识是否第一次组装包*/
	private boolean isFirst = true;
	
	private Handler handler;
	
	private int progress = 0;
	
	/** 照片描述*/
	private String description;
	
	/** Context对象*/
	private Context context;
	
	public CutFileUtil(Context context, String filePath, Handler handler, String description) throws FileNotFoundException {
		this.description = description;
		this.handler = handler;
		this.context = context;
		this.filePath = filePath;
		file = new File(filePath);
		if(!file.exists()) {
			throw new FileNotFoundException("File not exist!");
		}
		pieceFiles = new ArrayList<String>();
		//检测图片是否已经有切片
		if(isExistPieces()) {
			return;
		}
		cutFile();
	}
	
	/**
	 * 查找是否已有切片存在 
	 * @return
	 */
	public boolean isExistPieces() {
		String pieceName = StringUtil.convertFolderPath(filePath) + "_";
		int count = 0;
		Log.i(TAG, "Conver to piece name :" + pieceName);
		File folder = new File(Constant.PIECE_FOLDER);
		File[] files = folder.listFiles();
		String fileName = null;
		for(File file : files) {
			fileName = file.getName();
			if(fileName.contains(pieceName)) {
				count ++;
				Log.i(TAG, "Find exist piece file : " + file.getName());
			}
		}
		if(count > 0) {
			totalPieceNum = count;
			for(int i = 1; i <= count; i ++) {
				pieceFiles.add(Constant.PIECE_FOLDER + pieceName + i);
				Log.i(TAG, "pieceFiles[" + i + "] :" + Constant.PIECE_FOLDER + pieceName + i);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 文件切片
	 */
	private void cutFile() {
		try {
			FileInputStream in = new FileInputStream(filePath);
			fileSize = in.available();
			//计算切片总数
			int count = fileSize / pieceSize;
			totalPieceNum = (fileSize % pieceSize == 0) ? count : count + 1;
			Log.d(TAG, "total piece count : " + totalPieceNum + "; fileSize = " + fileSize);
			
			byte[] buf = new byte[pieceSize];
			int pieceNum = 1;
			int dataSize = 0;
			while(true) {
				if((dataSize = in.read(buf, 0, pieceSize - 90)) > 0) {
					Log.d(TAG, "dataSize " + dataSize);
					packagePiece(buf, pieceNum, dataSize);
					pieceNum ++ ;
					continue;
				} 
				break;
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
	 * @param buf 文件切片流
	 * @param pieceNum 当前包数
	 * @param dataSize 数据长度
	 * @throws IOException
	 */
	private void packagePiece(byte[] buf, int pieceNum, int dataSize) throws IOException {
		String pieceName = Constant.PIECE_FOLDER + StringUtil.convertFolderPath(filePath) + "_" +  pieceNum;
		Log.i(TAG, "piece file name : " + pieceName);
		FileOutputStream out = new FileOutputStream(pieceName);
		pieceFiles.add(pieceName);
		System.out.println("dataSize:" + dataSize);
		Log.i(TAG, "totalPieceNum : " + totalPieceNum + "; pieceNum" + pieceNum + "; dataSize" + dataSize);
		try {
			if(isFirst) {
				System.out.println("isFirst : aaaaaaaaaaa " + isFirst);
				packageHead = DataHeadUtil.getBytesHeadData(context, description, pieceNum, totalPieceNum, dataSize, false);
				isFirst = false;
			} else {
				packageHead = DataHeadUtil.getBytesHeadData(context, "", pieceNum, totalPieceNum, dataSize, true);
				System.out.println("isFirst : " + isFirst);
			}
		} catch (Exception e) {
			Toast.makeText(context, "转换包头信息出错！", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		out.write(packageHead);
		out.write(buf, 0, dataSize);
		out.close();
		Message msg = new Message();
		msg.obj = (Integer)(pieceNum  * 100 / totalPieceNum);
		msg.what = UploadFileActivity.PROGRESS_DIALOG;
		handler.sendMessage(msg);
	}
	
	/**
	 * 获取切片文件字节流
	 * @param buf 为NULL既可，内部将为buf自动分配内存空间
	 * @return -1表示文件已经读取完,否则返回读取buf的大小
	 * @throws IOException 
	 */
	public int getNextPiece(byte[] buf) {
		//如果文件名不存在，说明已经读完，则返回-1
		if(nCurrentPiece >= pieceFiles.size())
			return -1;
		String fileName = pieceFiles.get(nCurrentPiece);
		
		FileInputStream in;
		int pieceSizeTmp = 0;
		try {
			in = new FileInputStream(fileName);
			pieceSizeTmp = in.available();
			in.read(buf);
		} catch (Exception e) {
//			Toast.makeText(context, "获取切片失败！", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		
		nCurrentPiece ++;
		return pieceSizeTmp;
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
		FileInputStream in = new FileInputStream(fileName);
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
	
	public void removeAllPieceFile() {
		for(String filePath : pieceFiles) {
			File file = new File(filePath);
			file.delete();
		}
	}

	public int getTotalPieceNum() {
		return totalPieceNum;
	}

	public void setTotalPieceNum(int totalPieceNum) {
		this.totalPieceNum = totalPieceNum;
	}
	
	
	
}
