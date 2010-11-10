package com.camera.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.camera.activity.R;

/**
 * Gallary图片适配器
 * @author 郑澍璋
 */
public class ImageAdapter extends BaseAdapter {
	
    /** 图片资源*/
    private static List<Bitmap> mBitmaps;
    
    private Context mContext;
    
    /** Gallery背景*/
    private int mGalleryItemBackground;

    public ImageAdapter(Context c, List<Bitmap> bitmaps) {
    	this.mBitmaps = bitmaps;
        mContext = c;
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        a.recycle();
    }

    public int getCount() {
        return mBitmaps.size();
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
    public Bitmap getBitmap(int position) {
    	return mBitmaps.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(mBitmaps.get(position));
        imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);
        return imageView;
    }
}