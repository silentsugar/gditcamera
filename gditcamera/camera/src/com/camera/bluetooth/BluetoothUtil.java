package com.camera.bluetooth;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.Toast;

/**
 * 蓝牙发送工具类
 * @author 郑澍璋
 */
public class BluetoothUtil {

	/** 申请打开蓝牙*/
	public static final int REQUEST_BLUETOOTH = 1;
	/** 蓝牙默认扫描时间*/
	public static final int SCAN_SECONDS = 60;
	
	/** 蓝牙适配器*/
	private BluetoothAdapter mBluetoothAdapter;
	
	private Context mContext;
	
	public BluetoothUtil(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 要通过蓝牙发送的文件路径
	 * @param filePath 文件路径
	 */
	public void sendFile(String filePath) {
		
	}
	
	/**
	 * 初始化蓝牙配置环境
	 */
	public void initEnvironment() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
        	Toast.makeText(mContext, "您的设备不支持蓝牙功能！", Toast.LENGTH_SHORT).show();
        	return;
        } else {
        	if (!mBluetoothAdapter.isEnabled()) {
        		Builder builder = new Builder(mContext);
            	builder.setTitle("提示").setMessage("您的设备未开启蓝牙功能，现在是否启用？");
            	builder.setNegativeButton("取消", null);
            	//开启蓝牙设备
            	builder.setNeutralButton("确定", new OnClickListener()  {
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	        	    ((Activity)mContext).startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH);
    				}
            	});
            	builder.show();
        	}
        	
        }
	}
	
	/**
	 * 开启蓝牙扫描
	 */
	public boolean startDiscovery() {
		return mBluetoothAdapter.startDiscovery();
	}
	
	/**
	 * 停止蓝牙扫描
	 */
	public boolean cancelDiscovery() {
		return mBluetoothAdapter.cancelDiscovery();
	}
	/**
	 * 获取已匹配的蓝牙设备
	 * @return 已匹配的蓝牙设备
	 */
	public Set<BluetoothDevice> getPairedDevices() {
		if(mBluetoothAdapter == null)
			return null;
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		return pairedDevices;
	}
}
