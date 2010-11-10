package com.camera.activity;



import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.camera.adapter.FileListAdapter;
import com.camera.vo.FileItem;

public class SelectFolderActivity extends Activity implements OnItemClickListener,Button.OnClickListener{
	
	private ListView mFolderListView;
	private Button mChoose;
	private Button mCancel;
	private List<FileItem> mFolders;
	private String mSdcardPath;
	private File mDefaultFile;
	private File curreentFile;
	FileListAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefolder);
        mSdcardPath=Environment.getExternalStorageDirectory().getAbsolutePath();
        //取得界面的控件
        mFolderListView=(ListView)findViewById(R.id.lvFolder);
        mFolderListView.setOnItemClickListener(this);
        mChoose=(Button) this.findViewById(R.id.btnChoose);
        mCancel=(Button) this.findViewById(R.id.btnCancel);
        mChoose.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        
        mDefaultFile=new File(mSdcardPath);
        curreentFile=mDefaultFile;
        mFolders=new ArrayList<FileItem>();
        
        fileListView(mDefaultFile);
		
		
    }
	
	private void fileListView(File file){
		this.mFolders.clear();
		FileItem fileItem;
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				
				fileItem = new FileItem();
				fileItem.setTitle(files[i].getName());

				if (files[i].isDirectory()) {
					fileItem.setFlag(FileItem.FOLDER);
					fileItem.setImageResid(R.drawable.fileicon_dir);
					this.mFolders.add(fileItem);
				} 
			}
		}
		Collections.sort(mFolders);	
		if (file.getParentFile() != null&&!(file.getAbsolutePath().equals(mSdcardPath))) {
			fileItem = new FileItem();
			fileItem.setTitle("返回上级");
			fileItem.setImageResid(R.drawable.fileicon_back);
			mFolders.add(0, fileItem);
		}
		

		adapter = new FileListAdapter(this, this.mFolders);
		mFolderListView.setAdapter(adapter);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		String selectedFileName = this.mFolders.get(position).getTitle();
		File file ;
		//处理点击文件夹事件
		if(selectedFileName.equals("返回上级")){
			file = this.curreentFile.getParentFile();		
		}else{
			file=new File(this.curreentFile.getAbsolutePath()+"/"+selectedFileName);
		}
		this.curreentFile = file;
		fileListView(file);
	}

	@Override
	public void onClick(View v) {
		if(v==mChoose){
			//返回文件名
			Intent intent = new Intent();
			String path=this.curreentFile.getPath();
			intent.putExtra("path", path);
			setResult(Activity.RESULT_OK, intent);
			this.finish();
		}else{
			Intent intent = new Intent();
			setResult(Activity.RESULT_CANCELED, intent);
			this.finish();
		}
		
	}
}
