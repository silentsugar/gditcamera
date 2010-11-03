package com.camera.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.camera.adapter.ImageAdapter;

public class UploadFileActivity extends Activity implements OnClickListener {
	
	private Button mBtnUpdate;
	private Button mBtnUpdateAll;
	private EditText mTxtMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_filed);
		
		getComponents();
		
	}
	
	public void getComponents() {
		mBtnUpdate = (Button)this.findViewById(R.id.btnUpdate);
		mBtnUpdateAll = (Button)this.findViewById(R.id.btnUpdateAll);
		mTxtMessage = (EditText)this.findViewById(R.id.txtMessage);
		
		Gallery g = (Gallery) findViewById(R.id.gallery);
	    g.setAdapter(new ImageAdapter(this));

	    g.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}