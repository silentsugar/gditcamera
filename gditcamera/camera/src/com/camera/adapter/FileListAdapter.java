package com.camera.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.camera.activity.R;
import com.camera.vo.FileItem;

public class FileListAdapter extends BaseAdapter { 

	private List<FileItem> flieItems;
	FileItem fileItem;
	private Context context;
	LayoutInflater l;
	ImageView mImageView;
	TextView mTitile;
	ImageView flag;

	public FileListAdapter(Context context, List<FileItem> filesItems) {
		this.context = context;
		this.flieItems = filesItems;
		l = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return flieItems.size();
	}

	@Override
	public Object getItem(int position) {
		return flieItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int location, View view, ViewGroup viewGroup) {
		view = l.inflate(R.layout.file_item_list, null);
		mImageView = (ImageView) view.findViewById(R.id.item_icon);
		mTitile = (TextView) view.findViewById(R.id.item_title);
		fileItem = flieItems.get(location);
		mImageView.setBackgroundResource(fileItem.getImageResid());
		mTitile.setText(fileItem.getTitle());
		return view;
	}

}
