package com.camera.ui;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.activity.R;

public class DeviceItem extends RelativeLayout {
	
	public static final String TAG = "CatalogItem";
	
	private Context mContext;
	/** 布局Flater*/
	private LayoutInflater mLayoutInflater;
	/** TextView*/
	private TextView mTxtDeviceName;
	private Button mBtnSend;
	/** 当前目录名*/
	private String mCatalogName;
	/** 设备实体*/
	private BluetoothDevice mDevice;

	public DeviceItem(Context context, BluetoothDevice device) {
		super(context);
		this.mContext = context;
		this.mDevice = device;
		initContent();
	}

	/**
	 * 初始化布局
	 */
	private void initContent() {
		mLayoutInflater = LayoutInflater.from(this.mContext);
		this.setBackgroundResource(R.drawable.list_selector_background);
		this.addView(mLayoutInflater.inflate(R.layout.bluetooth_item, null), 
				LayoutParams.FILL_PARENT, 45);
		mTxtDeviceName = (TextView)this.findViewById(R.id.txtDeviceName);
		mBtnSend = (Button)this.findViewById(R.id.btnSend);
		if(mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
			mTxtDeviceName.setText(mDevice.getName() + "(未匹配)");
		} else if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
			mTxtDeviceName.setText(mDevice.getName() + "(已匹配)");
		}
		
	}

}
