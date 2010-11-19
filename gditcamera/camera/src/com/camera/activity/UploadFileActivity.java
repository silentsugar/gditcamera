package com.camera.activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.camera.adapter.ImageAdapter;
import com.camera.net.UploadFile;
import com.camera.picture.CutFileUtil;
import com.camera.picture.PictureUtil;
import com.camera.util.IniControl;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;
import com.camera.vo.Constant;
import com.camera.vo.UploadFileList;

/**
 * 图片上传管理模块
 * @author 郑澍璋
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	public static final int ACTIVITY_REQUEST_CODE = 1;
	
	private static String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	/** 刷新目录成功*/
	private static final int REFRESH_FOLDER_SUCCESS = 10;
	/** 刷新目录失败*/
	private static final int REFRESH_FOLDER_ERR = 11;
	/** 切片成功*/
	private static final int FINISH_CUT_FILE = 12;
	/** 切换进度对话框进度*/
	public static final int PROGRESS_DIALOG = 13;
	/** 上传多张图片的间隔时间*/
	public static final int UPLOAD_INTERVAL = 2000;
	/** 上传图片失败的重新上传延迟时间*/
	public static final int REUPLOAD_INTERVAL = 30000;
	
	
	/** 上传*/
	private Button mBtnUpload;
	/** 上传所有*/
	private Button mBtnUploadAll;
	/** 照片描述*/
	private EditText mTxtMessage;
	/** 图片浏览Gallery*/
	private Gallery mGallery;
	/** 图片适配器*/
	private ImageAdapter adapter;
	/** 对话框*/
	private ProgressDialog dialog;
	/** 当前选中图片的文件名*/
	private String mCurrentImg;
	private UploadFile uploadFile;
	private CutFileUtil cutFileUtil;
	
	/** 上传文件列表*/
	private UploadFileList mUploadFileList = new UploadFileList();
	
	private String mImagePath;
	
	
	Thread mUploadOnePicThread = new UploadThread();
	
	public class UploadThread extends Thread {
		
		private int mInterval = 0;
		
		public UploadThread() {
			super();
		}
		
		public UploadThread(int interval) {
			super();
			this.mInterval = interval;
		}
		
		@Override
		public void run() {
			synchronized (this) {
				try {
					this.sleep(mInterval);
					String description = mTxtMessage.getText().toString();
					cutFileUtil = new CutFileUtil(UploadFileActivity.this, mImagePath, mHandler, description);
					mHandler.sendEmptyMessage(FINISH_CUT_FILE);
					uploadFile = new UploadFile(UploadFileActivity.this, mHandler, this);
					uploadFile.upload(cutFileUtil);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					Log.e(TAG, "throw a exception while upload a file!!");
					e.printStackTrace();
					mHandler.sendEmptyMessage(UploadFile.THROW_EXCEPTION);
				}
			}
		}	
	};
	
	/**
	 * 处理异步线程信息
	 */
	Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			//图片目录刷新完
			super.handleMessage(msg);
			switch(msg.what) {
			//更新图片进度
			case PROGRESS_DIALOG:
				System.out.println((Integer)msg.obj);
				((ProgressDialog)dialog).setProgress((Integer)msg.obj);
				break;
				
			case FINISH_CUT_FILE:
				dialog.setMessage("正在连接服务器....");
				break;
				
			case UploadFile.TIME_OUT:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "连接服务器超时，上传图片 " + mImagePath + " 失败！", Toast.LENGTH_SHORT).show();
				uploadNextFile(false, false, REUPLOAD_INTERVAL);
				break;
			
			case UploadFile.CONNECTION_SUCCESS:
//				Toast.makeText(UploadFileActivity.this, "连接服务器成功！", Toast.LENGTH_SHORT).show();
				dialog.setMessage("正在上传图片....");
				break;
			case UploadFile.CONNECTION_FAILSE:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "连接服务器失败！", Toast.LENGTH_SHORT).show();
				uploadNextFile(false, false, REUPLOAD_INTERVAL);
				break;
				
			case UploadFile.CONNECT_TIME_OUT:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "接收服务器数据超时，上传图片 " + mImagePath + " 失败！", Toast.LENGTH_SHORT).show();
				uploadNextFile(false, true, REUPLOAD_INTERVAL);
				break;
			
			case UploadFile.FINISH_UPLOAD_FILE:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "成功上传图片  " + mImagePath + " ！", Toast.LENGTH_SHORT).show();
				uploadNextFile(true, false, UPLOAD_INTERVAL);
				break;
			case UploadFile.THROW_EXCEPTION:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "上传图片 " + mImagePath + " 时出现异常，上传失败！", Toast.LENGTH_SHORT).show();
				uploadNextFile(false, true, REUPLOAD_INTERVAL);
				break;
			//正在刷新目录
			case REFRESH_FOLDER_SUCCESS:
				adapter = new ImageAdapter(UploadFileActivity.this, PICTURE_FOLDER);
				mGallery.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				break;
			case REFRESH_FOLDER_ERR:
				Toast.makeText(UploadFileActivity.this, "刷新目录失败！", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			}
		}
	};
	
	/**
	 * 上传下一个文件
	 * @param isLastSuccess 文件发送是否成功
	 * @param isReSend 是否重新发送
	 * @param delay 延迟发送秒数
	 */
	public void uploadNextFile(boolean isLastSuccess, boolean isReSend, int delay) {
		if(mUploadFileList.size() < 1)
			return;
		Object item = mUploadFileList.get(0);
		if(isLastSuccess) {
			mUploadFileList.addSuccess(item);
		} else {
			mUploadFileList.addFailse(item);
		}
		if(!isReSend) {
			mUploadFileList.remove(item);
		}
		if(mUploadFileList.size() < 1)
			return;
		mImagePath = (String)mUploadFileList.get(0);
//		System.out.println("---------------------" + mImagePath + "----------------------------");
		showDialog();
		mUploadOnePicThread = new UploadThread(delay);
		mUploadOnePicThread.start();
	}
	
	/** 图片预览控件*/
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//初始化应用程序
		try {
			IniControl.initConfiguration(this);
			Toast.makeText(this, "初始化出现成功！", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			Toast.makeText(this, "初始化出现异常！", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		//检验配置文件是否存在，如果存在，则跳到配置界面
//		File file = new File(Constant.PERFERENCES_FILE_PATH);
//		if(!file.exists()) {
//			Intent intent = new Intent();
//			intent.setClass(this, ConfigurationActivity.class);
//			this.startActivity(intent);
//			return;
//		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_filed);
		//获取图片路径
		PreferencesDAO preferencesDao = new PreferencesDAO(this);
		PICTURE_FOLDER = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
		
		getComponents();
		loadPicture();
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * 获取控件
	 */
	public void getComponents() {
		mTxtMessage = (EditText)this.findViewById(R.id.txtMessage);
		mBtnUpload = (Button)this.findViewById(R.id.btnUpload);
		mBtnUploadAll = (Button)this.findViewById(R.id.btnUploadAll);
		mBtnUpload.setOnClickListener(this);
		mBtnUploadAll.setOnClickListener(this);
		//mTxtMessage = (EditText)this.findViewById(R.id.txtMessage);
		mImageView = (ImageView)this.findViewById(R.id.img);
		mGallery = (Gallery) findViewById(R.id.gallery);
	}
	
	/**
	 * 加载图片资源到Gallery
	 */
	public void loadPicture() {
		adapter = new ImageAdapter(this, PICTURE_FOLDER);
		mGallery.setAdapter(adapter);
		//默认选中第一项
		PictureUtil pictureUtil = new PictureUtil();
		if(adapter.getCount() > 0) {
			Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(0) + ".big");
	        mCurrentImg = adapter.getImagePath(0);
	        mImageView.setImageBitmap(bitmap);
	        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        mGallery.setSelection(0);
		}
		mGallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
//	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            PictureUtil pictureUtil = new PictureUtil();
	            Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(position) + ".big");
	            mCurrentImg = adapter.getImagePath(position);
	            mImageView.setImageBitmap(bitmap);
	            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        }
	    });
	}
	
	/**
	 * 单击事件处理
	 */
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		//上传一张图片
		case R.id.btnUpload:
			mImagePath = StringUtil.convertBackFolderPath(mCurrentImg);
			mUploadFileList.add(mImagePath);
			showDialog();
			mUploadOnePicThread = new UploadThread();
			mUploadOnePicThread.start();
			break;
		case R.id.btnUploadAll:
			int size = adapter.getCount();
			if(size > 0) {
				mImagePath = StringUtil.convertBackFolderPath(adapter.getImagePath(0));
			}
			for(int i = 0; i < size; i ++) {
				System.out.println(StringUtil.convertBackFolderPath(adapter.getImagePath(i)));
				mUploadFileList.add(StringUtil.convertBackFolderPath(adapter.getImagePath(i)));
			}
			showDialog();
			mUploadOnePicThread = new UploadThread();
			mUploadOnePicThread.start();
			break;
		}
	}
	
	public void showDialog() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		CharSequence title = "正在上传图片 " + mImagePath;
		// CharSequence message = getString(R.string.xxx);
		CharSequence message = "当前处理进度";
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setMessage("正在处理图片（切片）....");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setButton("后台运行", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		progressDialog.setProgress(0);
		progressDialog.setMax(100);
//		progressDialog.setOnCancelListener(mThread);
//		progressDialog.setOnDismissListener(mThread);
		progressDialog.show();
		dialog = progressDialog;
	}
	
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.menu, menu);
		  return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		//刷新目录图片
		case R.id.menuRefresh:
			startRefreshFolder();
			break;
		//跳到配置界面
		case R.id.menuConfig:
			Intent intent = new Intent();
			intent.setClass(this, ConfigurationActivity.class);
			this.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
			break;
		//退出系统
		case R.id.menuExit:
			Builder builder = new Builder(this);
			builder.setTitle("提示").setMessage("您是否要退出系统？");
			builder.setNegativeButton("确定", new Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNeutralButton("取消", null);
			builder.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 开启异步刷新目录
	 */
	public void startRefreshFolder() {
		dialog = ProgressDialog.show(this, "请稍候", 
                "正在刷新图片目录......", true);
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					refreshFolder();
					mHandler.sendEmptyMessage(REFRESH_FOLDER_SUCCESS);
				} catch(Exception e ){
					mHandler.sendEmptyMessage(REFRESH_FOLDER_ERR);
				}
				
			}	
		};
		thread.start();
	}
	
	/**
	 * 刷新目录图片，重新生成缩略图
	 */
	public void refreshFolder() throws Exception {
		try {
			//获取图片路径
			PreferencesDAO preferencesDao = new PreferencesDAO(this);
			PICTURE_FOLDER = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
			PictureUtil pictureUtil = new PictureUtil();
			pictureUtil.clearThumbnail(PICTURE_FOLDER);
			pictureUtil.createThumbnails(PICTURE_FOLDER);
		} catch (Exception e) {
			Log.e(TAG, "throw a exception while refresh the picture folder!");
			e.printStackTrace();
			throw new Exception("throw a exception while refresh the picture folder!");
		}
	}
	
	/**
	 * 获取配置界面传回的参数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case ACTIVITY_REQUEST_CODE:
			if(resultCode == Activity.RESULT_OK){
				PreferencesDAO preferencesDao = new PreferencesDAO(this);
				String folderPath = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
				if(!PICTURE_FOLDER.equals(folderPath)) {
					startRefreshFolder();
				}
			}
			break;
		}
	}
}