package com.camera.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.picture.CutFileUtil;

public class UploadFile extends Thread {
	
	public static final String TAG = "UploadFile";
	
	public static final int FINISH_UPLOAD_FILE = 1;

	/** 文件切片工具*/
	private CutFileUtil mCutFileUtil;
	/** 套接字线程*/
	private SocketManager mSocketManger;
	/** 界面handler*/
	private Handler mUIHandler;
	private Context mContext;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			//包发送成功的处理
			case SocketManager.PACKAGE_SEND_SUCCESS:
				Log.i(TAG, "PACKAGE_SEND_SUCCESS");
				break;
			//包发送失败的处理
			case SocketManager.PACKAGE_SEND_FAIL:
				Log.i(TAG, "PACKAGE_SEND_FAIL");
				break;
			//包发送超时的处理
			case SocketManager.TIME_OUT:
				Log.i(TAG, "TIME_OUT");
				break;
			}
		}
		
	};
	
	public UploadFile(Context context, Handler handler) {
		this.mContext = context;
		this.mUIHandler = handler;
		mSocketManger = new SocketManager(mContext, handler);
	}
	
	@Override
	public void run() {
		Log.i(TAG, "run....");
		byte[] dataBuf = new byte[CutFileUtil.pieceSize];
		int length = 0;
		//从切片对象中一片片获取文件流，上传到服务器
		int i = 0;
		while((length = mCutFileUtil.getNextPiece(dataBuf)) != -1) {
			Log.i(TAG, "send " + i++ + "piece");
			mSocketManger.send(dataBuf, length);
		}
		//文件上传完,通知界面已经上传好了一个文件
		Message msg = new Message();
		msg.what = FINISH_UPLOAD_FILE;
		mUIHandler.sendMessage(msg);
	}

	/**
	 * 上传文件
	 * @param cutFileUtil 文件切片对象
	 */
	public void upload(CutFileUtil cutFileUtil) {
		this.mCutFileUtil = cutFileUtil;
		mUIHandler.post(this);
		this.start();
	}
}
