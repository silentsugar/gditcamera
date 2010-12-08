package com.camera.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.camera.adapter.DeviceListAdapter;
import com.camera.bluetooth.BluetoothUtil;

public class BluetoolthActivity extends Activity implements OnClickListener {
	
	public static final int STOP_SCAN = 1;
	
	public static final String START_SCAN_TEXT = "扫描设备";
	public static final String STOP_SCAN_TEXT = "停止扫描";
    
	/** 蓝牙工具*/
	private BluetoothUtil mBtnTool;
	/** 蓝牙设备列表*/
	private ListView mLstDeviceList;
	/** 适配器*/
	private DeviceListAdapter mAdapter;
	/** 扫描设备按钮*/
	private Button mBtnScan;
	/** 扫描列表标题*/
	private TextView mTxtScanTitle;
	/** 蓝牙设备列表*/
	private List<BluetoothDevice> mDevicesList;
	/** 计时线程*/
	private CancleScanThread mCancleThread;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case STOP_SCAN:
				mBtnScan.setText(START_SCAN_TEXT);
				mTxtScanTitle.setText("扫描到的蓝牙设备");
				mBtnTool.cancelDiscovery();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 用于暂停蓝牙搜索
	 */
	private class CancleScanThread extends Thread {
		@Override
		public void run() {
			//默认扫描12秒钟
			try {
				Thread.sleep(60000);
				mBtnTool.cancelDiscovery();
				mHandler.sendEmptyMessage(STOP_SCAN);
			} catch (InterruptedException e) {}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);
        initContent();
    }
    
    /**
     * 初始化界面及设备
     */
    private void initContent() {
    	mBtnScan = (Button)this.findViewById(R.id.btnScanDevice);
    	mLstDeviceList = (ListView)this.findViewById(R.id.lstDeviceList);
    	mTxtScanTitle = (TextView)this.findViewById(R.id.txtScanDevices);
    	
    	mBtnTool = new BluetoothUtil(this);
        mBtnTool.initEnvironment();
        mDevicesList = new ArrayList<BluetoothDevice>(mBtnTool.getPairedDevices());
        mAdapter = new DeviceListAdapter(this, mDevicesList);
        mLstDeviceList.setAdapter(mAdapter);
        
        mBtnScan.setOnClickListener(this);
        
        /** 注册广播*/
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//蓝牙功能
		switch(requestCode){
		case BluetoothUtil.REQUEST_BLUETOOTH:
			if (resultCode == Activity.RESULT_OK){
				Toast.makeText(this, "已经打开蓝牙功能！", Toast.LENGTH_LONG).show();
			} else if(resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(this, "您的蓝牙并未开启，不能发送文件！", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
    
    /**
     * 蓝牙接收广播
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevicesList.add(device);
                mAdapter.setData(mDevicesList);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnScanDevice:
			if(mBtnScan.getText().toString().equals(START_SCAN_TEXT)) {
		        if(!mBtnTool.startDiscovery()) {
		        	mBtnTool.initEnvironment();
		        } else {
		        	mDevicesList.clear();
		        	mDevicesList = new ArrayList<BluetoothDevice>(mBtnTool.getPairedDevices());
		        	mAdapter.notifyDataSetChanged();
			        mBtnScan.setText(STOP_SCAN_TEXT);
			        mTxtScanTitle.setText("正在扫描蓝牙设备...");
			        mCancleThread = new CancleScanThread();
			        mCancleThread.start();
		        }
			} else {
				mBtnScan.setText(START_SCAN_TEXT);
				mTxtScanTitle.setText("扫描到的蓝牙设备");
				mBtnTool.cancelDiscovery();
				mCancleThread.interrupt();
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	
	
}