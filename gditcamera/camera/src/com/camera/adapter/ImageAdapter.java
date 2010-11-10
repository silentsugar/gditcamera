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
 * GallaryÕº∆¨  ≈‰∆˜
 * @author ÷£‰¯Ë∞
 */
public class ImageAdapter extends BaseAdapter {
	
    /** Õº∆¨◊ ‘¥*/
    private static List<Bitmap> mBitmaps;
    
    private Context mContext;
    
    /** Gallery±≥æ∞*/
    private int mGalleryItemBackground;

    public ImageAdapter(Context c) {
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(mBitmaps.get(position));
        imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);
        return imageView;
    }
}