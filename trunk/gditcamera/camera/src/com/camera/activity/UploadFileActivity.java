package com.camera.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.adapter.ImageAdapter;
import com.camera.net.UploadFile;
import com.camera.picture.CutFileUtil;
import com.camera.picture.PictureUtil;
import com.camera.util.Constant;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;

/**
 * 图片上传管理模块
 * @author 郑澍璋
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	
	private static String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	/** 刷新目录成功*/
	private static final int REFRESH_FOLDER_SUCCESS = 10;
	/** 刷新目录失败*/
	private static final int REFRESH_FOLDER_ERR = 11;
	/** 切片成功*/
	private static final int FINISH_CUT_FILE = 12;
	/** 切换进度对话框进度*/
	public static final int PROGRESS_DIALOG = 13;
	
	private Button mBtnUpload;
	private Button mBtnUploadAll;
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
				Toast.makeText(UploadFileActivity.this, "连接服务器超时，上传失败！", Toast.LENGTH_SHORT).show();
				break;
			
			case UploadFile.CONNECTION_SUCCESS:
				Toast.makeText(UploadFileActivity.this, "连接服务器成功！", Toast.LENGTH_SHORT).show();
				dialog.setMessage("正在上传图片....");
				break;
			case UploadFile.CONNECTION_FAILSE:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "连接服务器失败！", Toast.LENGTH_SHORT).show();
				break;
			
			
			case UploadFile.FINISH_UPLOAD_FILE:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "上传图片成功！", Toast.LENGTH_SHORT).show();
				break;
			case UploadFile.THROW_EXCEPTION:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "上传时出现异常，上传失败！", Toast.LENGTH_SHORT).show();
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
	
	/** 图片预览控件*/
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_filed);
		//获取图片路径
		PreferencesDAO preferencesDao = new PreferencesDAO(this);
		PICTURE_FOLDER = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
		
		getComponents();
		loadPicture();
		
	}
	
	/**
	 * 获取控件
	 */
	public void getComponents() {
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
		mGallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
//	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            PictureUtil pictureUtil = new PictureUtil();
	            Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(position) + ".big");
	            mCurrentImg = adapter.getImagePath(position);
	            mImageView.setImageBitmap(bitmap);
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
			showDialog();
			Thread uploadOnePicThread = new Thread() {
				@Override
				public void run() {
					try {
						String imagePath = StringUtil.convertBackFolderPath(mCurrentImg);
						System.out.println(imagePath);
						cutFileUtil = new CutFileUtil(UploadFileActivity.this, imagePath, mHandler);
						mHandler.sendEmptyMessage(FINISH_CUT_FILE);
						uploadFile = new UploadFile(UploadFileActivity.this, mHandler, this);
//						uploadFile.upload(cutFileUtil);
					} catch (Exception e) {
						Log.e(TAG, "throw a exception while upload a file!!");
						e.printStackTrace();
						mHandler.sendEmptyMessage(UploadFile.THROW_EXCEPTION);
					}
				}	
			};
			uploadOnePicThread.start();
			break;
		case R.id.btnUploadAll:
			break;
		}
	}
	
	public void showDialog() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		CharSequence title = "ProgressDialog监听线程处理进度";
		// CharSequence message = getString(R.string.xxx);
		CharSequence message = "当前处理进度";
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setMessage("正在切片....");
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
			mHandler.post(thread);
			thread.start();
			mHandler.removeCallbacks(thread);
			break;
		//跳到配置界面
		case R.id.menuConfig:
			Intent intent = new Intent();
			intent.setClass(this, Main.class);
			this.startActivity(intent);
			this.finish();
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
	 * 刷新目录图片，重新生成缩略图
	 */
	public void refreshFolder() throws Exception {
		try {
			PictureUtil pictureUtil = new PictureUtil();
			pictureUtil.clearThumbnail(PICTURE_FOLDER);
			pictureUtil.createThumbnails(PICTURE_FOLDER);
		} catch (Exception e) {
			Log.e(TAG, "throw a exception while refresh the picture folder!");
			e.printStackTrace();
			throw new Exception("throw a exception while refresh the picture folder!");
		}
	}
}