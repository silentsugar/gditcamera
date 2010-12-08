package com.camera.adapter;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.camera.ui.DeviceItem;

/**
 * 目录适配器
 * @author 郑澍璋
 */
public class DeviceListAdapter extends BaseAdapter {

	private Context mContext;
	/** 主目录数据*/
	private List<BluetoothDevice> mDevices;
	
	public DeviceListAdapter(Context context, List<BluetoothDevice> data){
		this.mDevices = data;
		this.mContext=context;
	}
	
	public void setData(List<BluetoothDevice> data) {
		this.mDevices = data;
	}

	@Override
	public int getCount() {
		if(mDevices == null)
			return 0;
		return mDevices.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		DeviceItem itemView = (DeviceItem)view;
		itemView = new DeviceItem(mContext, mDevices.get(position));
//		itemView.setOnItemClickListener(mOnItemClickListener);
		return itemView;
	}

}
