package com.camera.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.activity.R;

/**
 * 选项卡标签控件，支持垂直和水平布局，所有选项卡标签控件都必须
 * 通过调用CTabView.Builder.Create(CTabView view)方法进行创建
 * 否则不能正常使用
 * 
 * @author 郑澍璋
 */
public class CTabView extends RelativeLayout {
	
	/** 图像控件*/
	private View mImageView;
	/** 文字控件*/
	private TextView mTextView;
	/** 在内部增加多一个监听器*/
	private OnClickListener mInnerOnClickListener = null;
	
	private CTabView(Context context) {
		super(context);
		mImageView = new View(context);
		mTextView = new TextView(context);
	}
	
	public static class CTabViewFactory implements OnClickListener {
		
		/** 图像资源ID*/
		private int mImageResource = android.R.drawable.stat_sys_phone_call;
		/** 图像的尺寸大小,默认为30*30dip */
		private int[] mImageSize = {30, 30};
		/** 是否显示图像*/
		private boolean mIsShowImage = true;
		/** 图像占用的空间大小,垂直布局时为占用的高度，水平布局时为占用的宽度，默认为30dip*/
		private int mImageSpace = 60;
		
		/** 显示文本文字*/
		private String mText;
		/** 文字大小,默认字体大小为14*/
		private int mTextSize = 15;
		
		private Context mContext;
		/** 背景图片*/
		private int mBackgroundResource = R.drawable.tab_title_bg_normal;
		/** 点击后切换到另外一种样式*/
		private int mClickedBackground = R.drawable.tab_title_bg_press;
		/** 点击后是否切换到另外一种样式*/
		private boolean mIsShowClickedBackground = true;
		/** 横或竖的布局,默认为水平布局*/
		private boolean mOrientation = true;
		/** 控件的高度*/
		private int mHeight = 40;
		
		/** 选项卡控件集合*/
		private List<CTabView> mTabViewList;
		private CTabView mTabView;
		
		public CTabViewFactory(Context context) {
			this.mContext = context;
			mTabViewList = new ArrayList<CTabView>();
		}
		
		/**
		 * 创建一个CTabView对象，所有CTabView对象都必须通过该函数进行创建，否则不能使用
		 * @param tabView
		 * @return
		 */
		public CTabView create() {
			if(mTabView == null) {
				mTabView = new CTabView(mContext);
				mTabView.setBackgroundResource(mClickedBackground);
			} else {
				mTabView = new CTabView(mContext);
				mTabView.setBackgroundResource(mBackgroundResource);
			}
			mTabViewList.add(mTabView);
			initConext();
			//增加事件监听，实现选项一被点击，先换样式，再把其他的按钮换成默认样式
			mTabView.setInnerOnClickListener(this);
			return mTabView;
		}
		
		@Override
		public void onClick(View v) {
			if(mTabView.mInnerOnClickListener == null)
				return;
			if(!mIsShowClickedBackground)
				return;
			for(CTabView tv : mTabViewList) {
				if(!tv.equals(v))
					tv.setBackgroundResource(CTabViewFactory.this.mBackgroundResource);
				else {
					v.setBackgroundResource(CTabViewFactory.this.mClickedBackground);
					((CTabView)v).mInnerOnClickListener.onClick(v);
				}
			}
		}
		
		private void initConext() {
			LayoutParams lp = null;
			//总体设置
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, mHeight, 1);
			mTabView.setLayoutParams(lp2);
			
			//设置图像属性
			View imgView = mTabView.mImageView;
			imgView.setBackgroundResource(mImageResource);
			
			//设置文本控件属性
			TextView textView = mTabView.mTextView;
			textView.setText(mText);
			textView.setTextSize(mTextSize);
			textView.setTextColor(Color.WHITE);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
			
			//把文本控件和图像控件加到容器中
			if(mOrientation) {
				//水平状态下
				if(mIsShowImage) {
					int[] i = mImageSize;
					lp =  new LayoutParams(i[0], i[1]);
					int k = (mImageSpace - mImageSize[0]) / 2;
					lp.setMargins(k, 0, 0, 0);
					lp.addRule(RelativeLayout.CENTER_VERTICAL);
					
					mTabView.addView(imgView, lp);
				}
				lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				lp.setMargins(mImageSpace, 0, 0, 0);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				mTabView.addView(textView, lp);
				
			} else {
				//竖直状态下
				if(mIsShowImage) {
					int[] i = mImageSize;
					lp =  new LayoutParams(i[0], i[1]);
					int k = (mImageSpace - mImageSize[1]) / 2;
					lp.setMargins(0, k, 0, 0);
					lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					mTabView.addView(imgView, lp);
				}
				textView.setGravity(Gravity.CENTER);
				lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				lp.setMargins(0, mImageSpace, 0, 0);
				mTabView.addView(textView, lp);
			}
		}
		
		/**
		 * 选中当前某一项
		 * @param position
		 */
		public void setClicked(int position) {
			if(mTabViewList == null || mTabViewList.size() <= position || position < 0) {
				mTabViewList.get(position).setBackgroundResource(this.mClickedBackground);
			}
		}
		
		public void setBackgroundResource(int resid) {
			this.mBackgroundResource = resid;
		}

		/**
		 * 设置图像的尺寸，单位为dip
		 * @param width 宽度
		 * @param height 长宽
		 */
		public void setImageSize(int width, int height) {
			mImageSize[0] = width;
			mImageSize[1] = height;
		}

		/**
		 * 获取图像ID
		 */
		public int getImageResource() {
			return mImageResource;
		}

		/**
		 * 设置图像
		 */
		public void setImageResource(int mImageId) {
			this.mImageResource = mImageId;
		}
		
		/**
		 * 设置图像
		 * @param mImageId 图像ID
		 * @param width 图像的宽
		 * @param height 图像的高
		 */
		public void setImage(int mImageId, int width, int height) {
			mImageSize[0] = width;
			mImageSize[1] = height;
			setImageResource(mImageId);
		}

		
		/**
		 * 是否显示图像
		 */
		public boolean isShowImage() {
			return mIsShowImage;
		}

		/**
		 * 设置是否显示图像
		 * @param mIsShowImage
		 */
		public void setShowImage(boolean mIsShowImage) {
			this.mIsShowImage = mIsShowImage;
		}

		/**
		 * 获取文本大小
		 */
		public int getTextSize() {
			return mTextSize;
		}

		/**
		 * 设置文本大小
		 */
		public void setTextSize(int textSize) {
			this.mTextSize = textSize;
		}

		/**
		 * 设置横竖布局
		 */
		public void setOrientation(boolean orientation) {
			this.mOrientation = orientation;
		}

		/**
		 * 设置图像占用的空间大小,垂直布局时为占用的高度，水平布局时为占用的宽度，默认为30dip
		 */
		public int getImageSpace() {
			return mImageSpace;
		}

		/**
		 * 设置图像占用的空间大小,垂直布局时为占用的高度，水平布局时为占用的宽度，默认为30dip
		 */
		public void setImageSpace(int mImageSpace) {
			this.mImageSpace = mImageSpace;
		}

		/**
		 * 获取控件点击后显示的样式
		 */
		public int getClickedBackground() {
			return mClickedBackground;
		}
		
		/**
		 * 设置控件文本内容
		 */
		public CTabViewFactory setText(String text) {
			this.mText = text;
			return this;
		}

		/**
		 * 设置控件点击后显示的样式
		 */
		public void setClickedBackground(int mSelectedBackground) {
			this.mClickedBackground = mSelectedBackground;
		}

		/**
		 * 获取是否显示控件点击后切换到不同的背景样式
		 */
		public boolean isShowSelectedBackground() {
			return mIsShowClickedBackground;
		}

		/**
		 * 设置控件点击后是否切换到不同的样式
		 */
		public void setShowSelectedBackground(boolean mIsShowSelectedBackground) {
			this.mIsShowClickedBackground = mIsShowSelectedBackground;
		}

		public void setHeight(int mHeight) {
			this.mHeight = mHeight;
		}

	}
	
	/**
	 * 重写用户的事件监听器事件，内部监听器被内部类征用，由内部监听器执行完再触发用户的监听器
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mInnerOnClickListener = l;
	}
	
	public void setInnerOnClickListener(OnClickListener listener) {
		super.setOnClickListener(listener);
	}
	
}
