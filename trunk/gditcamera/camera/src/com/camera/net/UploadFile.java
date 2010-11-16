package com.camera.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.picture.CutFileUtil;

public class UploadFile {
	
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
			//文件发送成功
			case SocketManager.FINISH_UPLOAD_FILE:
				Log.i(TAG, "FINISH_UPLOAD_FILE");
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
	

	/**
	 * 上传文件
	 * @param cutFileUtil 文件切片对象
	 */
	public void upload(CutFileUtil cutFileUtil) {
		this.mCutFileUtil = cutFileUtil;
		mSocketManger.sendFile(cutFileUtil);
	}
}
