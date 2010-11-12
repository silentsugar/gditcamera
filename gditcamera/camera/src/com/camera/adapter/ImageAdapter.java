package com.camera.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.activity.R;
import com.camera.picture.PictureUtil;

/**
 * Gallary图片适配器
 * @author 郑澍璋
 */
public class ImageAdapter extends BaseAdapter {  
	
	public static final String TAG = "ImageAdapter";
	
	/** 图片目录*/
	private String mFolderPath;
    /** 图片资源路径*/
    private List<String> mPaths;
    /** 图片操作工具*/
    private PictureUtil pictureUtil;
    
    private Context mContext;
    
    /** Gallery背景*/
    private int mGalleryItemBackground;

    public ImageAdapter(Context context, String folderPath) {
    	this.mFolderPath = folderPath;
        mContext = context;
        
        //获取图片缩略图资源的路径
        pictureUtil = new PictureUtil();
        try {
        	mPaths = pictureUtil.getThumbnailPathsByFolder(folderPath);
		} catch (Exception e) {
			Toast.makeText(mContext, "查找缩略图文件时出错！", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        a.recycle();
    }
    
    public int getCount() {
        return mPaths.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    /**
     * 通过位置获取索引获取图片
     * @param position 位置索引
     * @return 图片资源
     */
    public String getImagePath(int position) {
    	return mPaths.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView = null;
		imageView = new ImageView(mContext);
        imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);
        
        //获取图片资源
        Bitmap bitmap = pictureUtil.getBitmap(mPaths.get(position));
        if(bitmap == null)
        	return null;
        imageView.setImageBitmap(bitmap);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}