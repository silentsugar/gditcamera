package com.camera.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.camera.util.Constant;
import com.camera.util.StringUtil;

/**
 * 图片上传管理模块
 * @author 郑澍璋
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	private static final String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	
	private static final int IS_REFRESH_FOLDER = 10;
	
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
	
	/**
	 * 处理异步线程信息
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//图片目录刷新完
			super.handleMessage(msg);
			switch(msg.what) {
			
			//正在刷新目录
			case IS_REFRESH_FOLDER:
				adapter = new ImageAdapter(UploadFileActivity.this, PICTURE_FOLDER);
				mGallery.setAdapter(adapter);
				adapter.notifyDataSetChanged();
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
	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
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
			String imagePath = StringUtil.convertBackFolderPath(mCurrentImg);
			System.out.println(imagePath);
			try {
				CutFileUtil cutFileUtil = new CutFileUtil(this, imagePath);
				UploadFile uploadFile = new UploadFile(this, mHandler);
				uploadFile.upload(cutFileUtil);
			} catch (Exception e) {
				Toast.makeText(this, "文件上传过程出现错误，错误原因：文件不存在或切片过程出现错误！", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			break;
		case R.id.btnUploadAll:
			break;
		}
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
					refreshFolder();
					mHandler.sendEmptyMessage(IS_REFRESH_FOLDER);
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
	public void refreshFolder() {
		try {
			PictureUtil pictureUtil = new PictureUtil();
			pictureUtil.clearThumbnail(PICTURE_FOLDER);
			pictureUtil.createThumbnails(PICTURE_FOLDER);
		} catch (Exception e) {
			Toast.makeText(this, "刷新目录出错了！", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}