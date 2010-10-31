package com.camera.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.camera.adapter.FileListAdapter;
import com.camera.util.FileUtil;
import com.camera.vo.FileItem;

public class SelectFolderActivity extends Activity implements OnItemClickListener,Button.OnClickListener{
	
	private ListView mFolderListView;
	private Button mChoose;
	private Button mCancel;
	private String reFolderName;
	private List<FileItem> mFolders;
	private String mSdcardPath;
	private File mDefaultFile;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefolder);
        mSdcardPath=Environment.getExternalStorageDirectory().getAbsolutePath();
        //取得界面的控件
        mFolderListView=(ListView) this.findViewById(R.id.lvFolder);
        mChoose=(Button) this.findViewById(R.id.btnChoose);
        mCancel=(Button) this.findViewById(R.id.btnCancel);
        mDefaultFile=new File(mSdcardPath);
        
        mFolders=new ArrayList<FileItem>();
        mFolderListView=new ListView(this);
        fileListView(mDefaultFile);
        FileListAdapter adapter=new FileListAdapter(this, this.mFolders);
		mFolderListView.setAdapter(adapter);
    }
	
	private void fileListView(File file){
		this.mFolders.clear();
		FileItem fileItem;
		fileItem=new FileItem();
		fileItem.setTitle("..返回上一级");
		fileItem.setImageResid(R.drawable.fileicon_back);
		this.mFolders.add(fileItem);
		File[] files=file.listFiles();
		for(int i=0;i<files.length;i++){
			if(files[i].isDirectory()){
				fileItem=new FileItem();
				fileItem.setTitle(files[i].getName());
				fileItem.setImageResid(R.drawable.fileicon_dir);
				this.mFolders.add(fileItem);
			}
		}
		Collections.sort(this.mFolders);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		String selectedFileName = this.mFolders.get(position).getTitle();
		//处理点击文件夹事件
		if(selectedFileName.equals("返回上级")){
			
		}else{
			
		}
	}

	@Override
	public void onClick(View v) {
		if(v==mChoose){
			//返回文件名
			
		}else{
			this.finish();
		}
		
	}
}
