package com.camera.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camera.adapter.ImageAdapter;
import com.camera.net.UploadFile;
import com.camera.picture.CutFileUtil;
import com.camera.picture.ImageCompress;
import com.camera.picture.PictureUtil;
import com.camera.util.IniControl;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;
import com.camera.vo.Constant;
import com.camera.vo.UploadFileList;
import com.camera.vo.VersionVo;

/**
 * 图片上传管理模块
 * @author 郑澍璋
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	
	public static final int ACTIVITY_REQUEST_CODE = 1;
	private static int NOTIFICATION_ID = 1;
	
	private static String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	/** 刷新目录失败*/
	private static final int REFRESH_FOLDER_ERR = 11;
	/** 清除缓存*/
	private static final int CLEAR_BUFFER = 17;
	/** 切片成功*/
	private static final int FINISH_CUT_FILE = 12;
	/** 切换进度对话框进度*/
	public static final int PROGRESS_DIALOG = 13;
	/** 切换进度对话框进度*/
	public static final int FILE_NOT_FIND = 14;
	/** 开始上传*/
	public static final int START_UPLOAD = 15;
	/** 压缩图片*/
	public static final int COMPRESS_PICTURE = 16;
	/** 刷新目录成功*/
	private static final int REFRESH_FOLDER_SUCCESS = 20;
	/** 上传多张图片的间隔时间*/
	public static final int UPLOAD_INTERVAL = 4000;
	/** 上传图片失败的重新上传延迟时间*/
	public static final int REUPLOAD_INTERVAL = 4000;
	
	
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
	/** 文件路径*/
	private TextView mTxtFilePath;
	/** 对话框*/
	private ProgressDialog dialog;
	/** 当前选中图片的文件名*/
	private String mCurrentImg;
	private String mCurrentFileName;
	private UploadFile uploadFile;
	private CutFileUtil cutFileUtil;
	
	/** 标识是否正在上传*/
	private boolean isUploading = false;
	
	private NotificationManager mNotificationManager;
	
	/** 上传文件列表*/
	private UploadFileList mUploadFileList = new UploadFileList();
	
	private String mImagePath;
	private String mImageName;
	
	
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
					isUploading = true;
					this.sleep(mInterval);
					mHandler.sendEmptyMessage(START_UPLOAD);
					String description = mTxtMessage.getText().toString();
					String imagePath = ImageCompress.compressJPG(mImagePath);
					mHandler.sendEmptyMessage(COMPRESS_PICTURE);
					cutFileUtil = new CutFileUtil(UploadFileActivity.this, imagePath, mHandler, description);
					mHandler.sendEmptyMessage(FINISH_CUT_FILE);
					uploadFile = new UploadFile(UploadFileActivity.this, mHandler, this);
					uploadFile.upload(cutFileUtil);
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(FILE_NOT_FIND);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					Log.e(TAG, "throw a exception while upload a file!!");
					e.printStackTrace();
//					mHandler.sendEmptyMessage(UploadFile.THROW_EXCEPTION);
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
				if(dialog.isShowing())
					((ProgressDialog)dialog).setProgress((Integer)msg.obj);
				break;
			case CLEAR_BUFFER:
				/** 刷新目录失败*/
				dialog.setMessage("清除缓存成功，正在刷新目录......");
				break;
			
			case FILE_NOT_FIND:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "未找到文件 " + mImageName + " ，请尝试刷新一下！", Toast.LENGTH_SHORT).show();
				break;
			case COMPRESS_PICTURE:
				dialog.setMessage("正在处理图片(切片)....");
				dialog.setProgress(0);
				break;
			case FINISH_CUT_FILE:
				dialog.setProgress(0);
				dialog.setMessage("服务器" + UploadFile.CURRENT_FILE_INDEX + ":正在连接服务器....");
				break;
				
			case START_UPLOAD:
				if(!dialog.isShowing()) {
					dialog.show();
				}
				break;
				
			case UploadFile.FINISH_SEND_FIRST_SERVER:
				if(!dialog.isShowing()) {
					dialog.show();
					dialog.setMessage("服务器" + UploadFile.CURRENT_FILE_INDEX + ": 开始上传图片  " + mImageName + " ");
				}
				break;
				
			case UploadFile.DISMISS_DIALOG:
				if(dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
				
			case CutFileUtil.MSG_EXIST_PIECE:
				Builder builder = new Builder(UploadFileActivity.this);
				builder.setTitle("提示").setMessage("系统检测到该文件上次未上传完，是否继续上传上次未完成的任务？");
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CutFileUtil.IS_SEND_LAST_PIECE = false;
						mUploadOnePicThread.interrupt();
					}
				});
				builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CutFileUtil.IS_SEND_LAST_PIECE = true;
						mUploadOnePicThread.interrupt();
					}
				});
				builder.show();
				break;
				
			case UploadFile.TIME_OUT:
				Toast.makeText(UploadFileActivity.this, "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 连接服务器超时，上传图片 " + mImageName + " 失败！", Toast.LENGTH_SHORT).show();
				sendNotification("上传失败", "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 连接服务器超时，上传图片 " + mImageName + " 失败！");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, false, REUPLOAD_INTERVAL);
				}
				break;
			
			case UploadFile.CONNECTION_SUCCESS:
//				Toast.makeText(UploadFileActivity.this, "连接服务器成功！", Toast.LENGTH_SHORT).show();
				dialog.setMessage("服务器" + UploadFile.CURRENT_FILE_INDEX + ": 正在上传图片到服务器....");
				dialog.setProgress(0);
				break;
			case UploadFile.CONNECTION_FAILSE:
				Toast.makeText(UploadFileActivity.this, "服务器" + UploadFile.CURRENT_FILE_INDEX + ":连接服务器 失败！", Toast.LENGTH_SHORT).show();
				sendNotification("上传失败", "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 连接服务器失败！");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, false, REUPLOAD_INTERVAL);
				}
				break;
				
			case UploadFile.FINISH_SEND:
				if(dialog.isShowing()) {
					dialog.dismiss();
				}
				uploadNextFile(false, false, REUPLOAD_INTERVAL);
				break;
				
			case UploadFile.CONNECT_TIME_OUT:
				Toast.makeText(UploadFileActivity.this, "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 接收服务器回复超时，上传图片 " + mImageName + " 失败！", Toast.LENGTH_SHORT).show();
				sendNotification("上传失败", "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 接收服务器回复超时，上传图片 " + mImageName + " 失败！");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, true, REUPLOAD_INTERVAL);
				}
				break;
			
			case UploadFile.FINISH_UPLOAD_FILE:
				Toast.makeText(UploadFileActivity.this, "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 成功上传图片  " + mImageName + " ！", Toast.LENGTH_SHORT).show();
				sendNotification("上传成功", "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 成功上传图片  " + mImageName + "到服务器！");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(true, false, UPLOAD_INTERVAL);
				}
				break;
			case UploadFile.THROW_EXCEPTION:
				Toast.makeText(UploadFileActivity.this, "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 上传图片 " + mImageName + " 到服务器时出现异常，上传失败！", Toast.LENGTH_SHORT).show();
				sendNotification("上传失败", "服务器" + UploadFile.CURRENT_FILE_INDEX + ": 上传图片 " + mImageName + " 到服务器时出现异常，上传失败！");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, false, REUPLOAD_INTERVAL);
				}
				break;
			//正在刷新目录
			case REFRESH_FOLDER_SUCCESS:
				adapter = new ImageAdapter(UploadFileActivity.this, PICTURE_FOLDER);
				mGallery.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				//选中第一项
				PictureUtil pictureUtil = new PictureUtil();
				if(adapter.getCount() > 0) {
					Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(0) + ".big");
			        mCurrentImg = adapter.getImagePath(0);
			        mCurrentFileName = StringUtil.convertBackFolderPath(mCurrentImg);
			        mCurrentFileName = StringUtil.getFileName(mCurrentFileName);
			        mTxtFilePath.setText("文件名：" + mCurrentFileName);
			        mImageView.setImageBitmap(bitmap);
			        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			        mGallery.setSelection(0);
				} else {
					mImageView.setImageResource(0);
				}
				break;
			case REFRESH_FOLDER_ERR:
				Toast.makeText(UploadFileActivity.this, "刷新目录失败！", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			}
		}
	};
	
	public void sendNotification(String title, String message) {
		
		if(mNotificationManager == null) {
			String ns = Context.NOTIFICATION_SERVICE;
			mNotificationManager = (NotificationManager) getSystemService(ns);
		}
				
		int icon = R.drawable.icon;        // icon from resources
		CharSequence tickerText = "camera";              // ticker-text
		long when = System.currentTimeMillis();         // notification time
		Context context = getApplicationContext();      // application Context

		Intent notificationIntent = new Intent(this, UploadFileActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, title, message, contentIntent);
		
		mNotificationManager.notify(NOTIFICATION_ID ++, notification);
	}
	
	/**
	 * 上传下一个文件
	 * @param isLastSuccess 文件发送是否成功
	 * @param isReSend 是否重新发送
	 * @param delay 延迟发送秒数
	 */
	public void uploadNextFile(boolean isLastSuccess, boolean isReSend, int delay) {
		if(mUploadFileList.size() < 1) {
			isUploading = false;
			return;
		}
		Object item = mUploadFileList.get(0);
		if(isLastSuccess) {
			mUploadFileList.addSuccess(item);
			mUploadFileList.remove(item);
		} else {
			mUploadFileList.addFailse(item);
			if(!isReSend) {
				mUploadFileList.remove(item);
			}
		}
		if(mUploadFileList.size() < 1) {
			isUploading = false;
			return;
		}
		mImagePath = (String)mUploadFileList.get(0);
		mImageName = StringUtil.getFileName(mImagePath);
//		System.out.println("---------------------" + mImagePath + "----------------------------");
		showDialog();
		Log.i(TAG, "--------------uploadNextFile:Send next picture-----------------");
		dialog.dismiss();
		mUploadOnePicThread = new UploadThread(delay);
		Log.w(TAG, "Send next picture; mUploadFileList size : " + mUploadFileList.size());
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
		startRefreshFolder();
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
		mTxtFilePath = (TextView)this.findViewById(R.id.txtFilePath);
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
		mGallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
//	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            PictureUtil pictureUtil = new PictureUtil();
	            Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(position) + ".big");
	            mCurrentImg = adapter.getImagePath(position);
	            mCurrentFileName = StringUtil.convertBackFolderPath(mCurrentImg);
	            mCurrentFileName = StringUtil.getFileName(mCurrentFileName);
	            mImageView.setImageBitmap(bitmap);
	            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            mTxtFilePath.setText("文件名：" + mCurrentFileName);
	            mTxtFilePath.setSelected(true);
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
			if(isUploading) {
				Toast.makeText(this, "当前有上传任务正在执行，请等待任务完成后再上传！", 4000).show();
				return;
			}
			mImagePath = StringUtil.convertBackFolderPath(mCurrentImg);
			mImageName = StringUtil.getFileName(mImagePath);
			mUploadFileList = new UploadFileList();
			mUploadFileList.add(mImagePath);
			showDialog();
			mUploadOnePicThread = new UploadThread();
			mUploadOnePicThread.start();
			break;
		case R.id.btnUploadAll:
			if(isUploading) {
				Toast.makeText(this, "当前有上传任务正在执行，请等待任务完成后再上传！", 4000).show();
				return;
			}
			int size = adapter.getCount();
			if(size > 0) {
				mImagePath = StringUtil.convertBackFolderPath(adapter.getImagePath(0));
				mImageName = StringUtil.getFileName(mImagePath);
			}
			mUploadFileList = new UploadFileList();
			for(int i = 0; i < size; i ++) {
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
		CharSequence title = "正在上传图片 " + mImageName;
		// CharSequence message = getString(R.string.xxx);
		CharSequence message = "当前处理进度";
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setMessage("正在压缩图片....");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setButton("后台运行", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				i.addCategory(Intent.CATEGORY_HOME);
				startActivity(i);
			}
		});
		progressDialog.setButton2("隐藏对话框", new Dialog.OnClickListener() {
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
//		case R.id.menuNotification:
//			
		//刷新目录图片
		case R.id.menuRefresh:
			startRefreshFolder();
			break;
		//跳到配置界面
		case R.id.menuConfig:
			Intent intent = new Intent();
			if(Integer.parseInt(android.os.Build.VERSION.SDK) > 3) {
				intent.setClass(this, ConfigurationActivity.class);
			} else {
				intent.setClass(this, ConfigurationActivity2.class);
			}
			this.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
			break;
		case R.id.menuClear:
			dialog = ProgressDialog.show(this, "请稍候",
	                "正在清除缓存文件......", true);
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						PictureUtil pictureUtil = new PictureUtil();
						pictureUtil.clearImagePieces();
						pictureUtil.clearThumbnail(PICTURE_FOLDER);
						mHandler.sendEmptyMessage(CLEAR_BUFFER);
						pictureUtil.createThumbnails(UploadFileActivity.this, PICTURE_FOLDER);
						refreshFolder();
						mHandler.sendEmptyMessage(REFRESH_FOLDER_SUCCESS);
					} catch(Exception e ){
						e.printStackTrace();
						mHandler.sendEmptyMessage(REFRESH_FOLDER_ERR);
					}
					
				}	
			};
			thread.start();
			break;
		case R.id.menuAbout:
			Builder builder = new Builder(UploadFileActivity.this);
			builder.setTitle("关于");
			String msg = "";
			msg += "当前版本： " + VersionVo.VERSION_NAME + "\n";
			msg += "版本名称： " + VersionVo.VERSION_DESC + "\n";

			msg += "发布日期： " + VersionVo.PUBLIC_DATE + "\n";
			msg += "新版本修改： \n";
			msg += VersionVo.UPLOAD_INFO;
			builder.setMessage(msg);
			builder.setNegativeButton("确定", null);
			builder.show();
			break;
		//退出系统
		case R.id.menuExit:
			exit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 退出程序
	 */
	public void exit() {
		Builder builder = new Builder(this);
		builder.setTitle("提示").setMessage("您是否要退出系统？");
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(isUploading) {
					Builder builder = new Builder(UploadFileActivity.this);
					builder.setTitle("提示").setMessage("当前有上传任务正在运行，是否停止上传任务并退出系统？");
					builder.setNegativeButton("后台运行", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(Intent.ACTION_MAIN);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
							i.addCategory(Intent.CATEGORY_HOME);
							startActivity(i);
						}
					});
					
					builder.setNeutralButton("退出", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					});
					builder.show();
					return;
				} else {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}
		});
		builder.show();
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
					e.printStackTrace();
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
//			pictureUtil.clearThumbnail(PICTURE_FOLDER);
			pictureUtil.createThumbnails(this, PICTURE_FOLDER);
//			pictureUtil.clearImagePieces();
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			exit();
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}