package com.camera.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.camera.widget.IListener.IOnItemClickListener;

/**
 * 工具栏控件，实现把几个Button控件集合到工具栏的效果
 * @author 郑澍璋
 */
public class CToolbar extends LinearLayout {
	
	/**---------------工具栏的布局-------------**/
	//public static LayoutParams lp = new LayoutParams
	
	//工具栏中控件的默认样式
	private int mItemStyle;
	//工具栏中控件被点击后的样式
	private int mSelectedStyle;
	//工具栏中控件被点击后是否显示不同的样式
	private boolean mIsShowSelectedStyle = true;
	//工具栏按钮单击事件
	private IOnItemClickListener mOnItemClickListener;
	//工具栏的控件集合
	private List<View> mListView = new ArrayList<View>();

	public CToolbar(Context context) {
		super(context);
	}
	
	/**
	 * 构造函数
	 * @param itemStyle 工具栏控件的默认样式
	 * @param selectedStyle 工具栏控件被点击后的样式
	 * @param isShowSelectedStyle 工具栏中控件被点击后是否显示不同的样式
	 */
	public CToolbar(Context context, int itemStyle, 
			int selectedStyle, boolean isShowSelectedStyle) {
		super(context);
		this.mItemStyle = itemStyle;
		this.mSelectedStyle = selectedStyle;
		this.mIsShowSelectedStyle = isShowSelectedStyle;
	}
	
	public CToolbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 向工具栏添加一个控件，并自动为新控件设置样式
	 */
	public void addItem(View child) {
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1.0f;
		child.setBackgroundResource(mItemStyle);
		child.setLayoutParams(lp);
		super.addView(child);
		mListView.add(child);
		dispatchItemClickEvent(child);
	}
	
	/**
	 * 向工具栏添加一个控件，并设置新控件的样式，该函数允许子控件有自己另外的样式和相对父容器的布局
	 * @param child 子控件
	 * @param lp 相对于父容器的布局，如果为NULL时而应用默认布局
	 * @param styleId 控件样式 控件的样式ID，如果为0时则应用默认的样式
	 */
	public void addItem(View child, LayoutParams lp, int styleId) {
		//应用样式
		if(styleId == 0) {
			styleId = mItemStyle;
		}
		child.setBackgroundResource(styleId);
		
		//应用布局
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1.0f;
		}
		child.setLayoutParams(lp);
		
		super.addView(child);
		mListView.add(child);
		dispatchItemClickEvent(child);
	}
	
	/**
	 * 获取容器中的控件，如果没有该控件，则返回NULL
	 * @param index 控件的序号
	 * @return 获取到的控件
	 */
	public View getItem(int index) {
		return mListView.get(index);
	}
	
	/**
	 * 从工具栏中移除控件
	 * @param child 要移除的子控件
	 */
	public void removeItem(View child) {
		this.removeView(child);
		mListView.remove(child);
	}
	
	/**
	 * 从工具栏中移除控件
	 * @param child 要移除的子控件序号
	 */
	public void removeItem(int index) {
		this.removeView(this.getChildAt(index));
		mListView.remove(index);
	}
	
	/**
	 * 移除所有工具栏上的控件
	 */
	public void removeAllItems() {
		this.removeAllViews();
		mListView.clear();
	}

	/**
	 * 为每一个新加进来的控件添加监听事件，并由mOnItemClickListener.onItemClick统一处理
	 * @param view 新增的控件
	 */
	protected void dispatchItemClickEvent(View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				
				//显示在工具栏按钮点击后按钮的样式
				if(mIsShowSelectedStyle) {
					if(mSelectedStyle != 0) {
						view.setBackgroundResource(mSelectedStyle);
						for(View v : mListView) {
							if(!v.equals(view)) {
								v.setBackgroundResource(mItemStyle);
							}
						}
					}
				}
				
				if(mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(mListView, view, mListView.indexOf(view), view.getId());
				}
			}
		});
	}
	
	/**
	 * 刷新工具栏的应用
	 */
	public void refreshChange() {
		
	}

	
	/**
	 * 获取子控件的统一样式
	 */
	public int getItemStyle() {
		return mItemStyle;
	}

	/**
	 * 设置子控件的统一样式
	 */
	public void setItemStyle(int mItemStyle) {
		this.mItemStyle = mItemStyle;
	}

	/**
	 * 获取点击子控件后控件的样式
	 */
	public int getSelectedStyle() {
		return mSelectedStyle;
	}

	/**
	 * 设置点击子控件后控件的样式
	 */
	public void setSelectedStyle(int mSelectedStyle) {
		this.mSelectedStyle = mSelectedStyle;
	}

	/**
	 * 工具栏中控件被点击后是否显示不同的样式
	 */
	public boolean isShowSelectedStyle() {
		return mIsShowSelectedStyle;
	}

	/**
	 * 设置栏中控件被点击后是否显示不同的样式
	 */
	public void setShowSelectedStyle(boolean isShowSelectedStyle) {
		this.mIsShowSelectedStyle = isShowSelectedStyle;
	}

	/**
	 * 设置子控件的监听事件
	 */
	public void setOnItemClickListener(IOnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

}
