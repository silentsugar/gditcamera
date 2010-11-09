package com.camera.widget;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.activity.R;

/**
 * 图像文本控件，支持垂直和水平布局
 * @author 郑澍璋
 */
public class CImageTextView extends RelativeLayout implements OnClickListener {
	
	/** 默认padding大小*/
	private static final int[] PADDING = {5, 10, 0, 10};
	/** 默认背景样式*/
	private static final int BACKGROUND_RESOURCE = R.drawable.button_bg;

	
	/** 图像资源ID*/
	private int mImageResource;
	/** 显示文本文字*/
	private String mText;
	/** 图像的尺寸大小,默认为30*30dip */
	private int[] mImageSize = {30, 30};
	/** 横或竖的布局,默认为水平布局*/
	private boolean mOrientation = true;
	/** 文字控件*/
	private TextView mTextView;
	/** 图像控件*/
	private View mImageView;
	/** 是否显示图像*/
	private boolean mIsShowImage = true;
	private Context mContext;
	/** 文字大小,默认字体大小为14*/
	private int mTextSize = 15;
	/** 图像占用的空间大小,垂直布局时为占用的高度，水平布局时为占用的宽度，默认为30dip*/
	private int mImageSpace = 40;
	/** 点击后切换到另外一种样式*/
	private int mClickedBackground;
	/** 点击后是否切换到另外一种样式*/
	private boolean mIsShowClickedBackground = false;
	/** 如果该控件被用来做tab的切换卡，则用该List保存其他选项卡的引用*/
	private List<View> mListSpecView;
	/** 切换卡默认背景，公当该控件被用来做TAB切换卡才会用上*/
	private int mBackgroundResource;
	
	private OnClickListener mOnClickListener2 = null;
	
	public CImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		//设置默认的属性
		setDefaultPropertie(mOrientation);
		//从XML文件中读取自定义属性
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CImageTextView);
		mImageResource = a.getResourceId(R.styleable.CImageTextView_image, R.drawable.tab_title_bg_normal);
		mText = a.getString(R.styleable.CImageTextView_text);
		mTextSize = a.getDimensionPixelSize(R.styleable.CImageTextView_textSize, mTextSize);
		mImageSize[0] = a.getDimensionPixelSize(R.styleable.CImageTextView_image_width, mImageSize[0]);
		mImageSize[1] = a.getDimensionPixelSize(R.styleable.CImageTextView_image_height, mImageSize[1]);
		mOrientation = a.getBoolean(R.styleable.CImageTextView_orientation, mOrientation);
		mImageSpace = a.getDimensionPixelSize(R.styleable.CImageTextView_imageSpace, mImageSpace);
		a.recycle();
		//加入图标
		this.setImageResource(mImageResource);
		//加入文字
		this.setText(mText);
	}
	
	/**
	 * 构造函数
	 * @param imageId 图像ID
	 * @param text 文本字体
	 * @param orientation 垂直(false)或水平(true)布局
	 */
	public CImageTextView(Context context, int imageId, String text, boolean orientation) {
		super(context);
		this.mContext = context;
		this.mOrientation = orientation;
		this.mImageResource = imageId;
		this.mText = text;
		this.mOrientation = orientation;
		//设置默认的属性
		setDefaultPropertie(orientation);
		//加入图标
		this.setImageResource(imageId);
		//加入文字
		this.setText(text);
		
	}
	
	/**
	 * 构造函数
	 * @param imageView 图像控件
	 * @param textView 文本控件
	 * @param orientation 垂直(false)或水平(true)布局
	 */
	public CImageTextView(Context context, View imageView, TextView textView, boolean orientation) {
		super(context);
		setDefaultPropertie(orientation);
		this.addView(imageView);
		this.addView(textView);
	}
	
	/**
	 * 构造函数,默认为水平布局
	 * @param imageId 图像ID
	 * @param text 文本字体
	 */
	public CImageTextView(Context context, int imageId, String text) {
		this(context, imageId, text, true);
	}
	
	/**
	 * 构造函数
	 * @param imageView 图像控件
	 * @param textView 文本控件
	 */
	public CImageTextView(Context context, View imageView, TextView textView) {
		this(context, imageView, textView, true);
	}
	
	/**
	 * 默认构造函数
	 */
	public CImageTextView(Context context) {
		super(context);
		this.mContext = context;
		setDefaultPropertie(true);
	}
	
	/**
	 * 设置该控件默认的属性
	 */
	public void setDefaultPropertie(boolean orientation) {
		this.setFocusable(true);
		int i[] = PADDING;
		this.setPadding(i[0], i[1], i[2], i[3]);
		super.setOnClickListener(this);
		this.setBackgroundResource(BACKGROUND_RESOURCE);
	}
	
	/**
	 * 设置图像的尺寸，单位为dip
	 * @param width 宽度
	 * @param height 长宽
	 */
	public void setImageSize(int width, int height) {
		mImageSize[0] = width;
		mImageSize[1] = height;
		if(mImageView != null) {
			this.removeView(mImageView);
			this.setImageView(mImageView);
		}
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
		if(mImageView == null) {
			mImageView = new View(mContext);
			this.setImageView(mImageView);
		}
		mImageView.setBackgroundResource(mImageId);
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
	 * 获取控件的文本
	 */
	public String getText() {
		if(mTextView != null) {
			return mTextView.getText().toString();
		}
		return null;
	}

	/**
	 * 设置文本的控件
	 */
	public void setText(String mText) {
		if (mText == null) {
			return;
		}
		this.mText = mText;
		if (mTextView == null) {
			mTextView = new TextView(mContext);
			mTextView.setTextSize(mTextSize);
			mTextView.setTextColor(Color.WHITE);
			this.setTextView(mTextView);
		}
		mTextView.setText(mText);
	}

	/**
	 * 获取文本控件
	 */
	public TextView getTextView() {
		return mTextView;
	}

	/**
	 * 设置文本控件
	 * @param mTextView
	 */
	public void setTextView(TextView mTextView) {
		this.mTextView = mTextView;
		
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lp.alignWithParent = true;
		//切换到水平布局
		if(mOrientation) {
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
			lp.setMargins(mImageSpace, 0, 0, 0);
			mTextView.setGravity(Gravity.CENTER_VERTICAL);
		} else {
			lp.addRule(RelativeLayout.ALIGN_BOTTOM);
			lp.alignWithParent = true;
			lp.setMargins(0, mImageSpace, 0, 0);
			mTextView.setGravity(Gravity.CENTER);
		}
		this.addView(mTextView, lp);
	}

	/**
	 * 获取图像控件
	 */
	public View getImageView() {
		return mImageView;
	}

	/**
	 * 设置图像控件
	 * @param mImageView
	 */
	public void setImageView(View mImageView) {

		LayoutParams lp = new LayoutParams(
				mImageSize[0], 
				mImageSize[1]);
		lp.alignWithParent = true;
		//切换到水平布局
		if(mOrientation) {
			lp.setMargins(this.getPaddingLeft(), 0, 0, 0);
			lp.addRule(RelativeLayout.ALIGN_LEFT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
		} else {
			lp.setMargins(0, this.getPaddingTop(), 0, 0);
			lp.addRule(RelativeLayout.ALIGN_TOP);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		this.addView(mImageView, lp);
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
		if(mImageView != null) {
			if(mIsShowImage) {
				this.setImageView(mImageView);
			} else {
				this.removeView(mImageView);
			}
		}
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
		if(mTextView != null) {
			mTextView.setTextSize(textSize);
		}
	}

	/**
	 * 设置横竖布局
	 */
	public void setOrientation(boolean orientation) {
		if(mImageView == null || mTextView == null) {
			return;
		}
		this.mOrientation = orientation;
		fleshLayout();
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
		fleshLayout();
	}
	
	/**
	 * 根据所设参数，刷新布局
	 */
	private void fleshLayout() {
		if(mImageView != null) {
			this.removeView(mImageView);
			this.setImageView(mImageView);
		}
		if(mTextView != null) {
			this.removeView(mTextView);
			this.setTextView(mTextView);
		}
	}

	/**
	 * 获取控件点击后显示的样式
	 */
	public int getClickedBackground() {
		return mClickedBackground;
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

	/**
	 * 重写用户的事件监听器事件，内部监听器被内部类征用，由内部监听器执行完再触发用户的监听器
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mOnClickListener2 = l;
	}

	@Override
	public void onClick(View v) {
		//先执行内部的单击逻辑，再执行用户的单击逻辑
		if(mClickedBackground > 0 && mIsShowClickedBackground) {
			this.setBackgroundResource(mClickedBackground);
		}
		if(mListSpecView != null) {
			for(View view : mListSpecView) {
				if(!view.equals(this))
					view.setBackgroundResource(this.mBackgroundResource);
			}
		}
		if(mOnClickListener2 != null)
			mOnClickListener2.onClick(v);
	}
	
	/**
	 * 如果该控件被用作tab选项卡，方可用到该方法
	 * @param specViewList
	 */
	public void setSpecList(List<View> specViewList, int backgroundResource) {
		this.mListSpecView = specViewList;
		this.mBackgroundResource = backgroundResource;
	}
	
	/**
	 * 设置控件的背景样式为单击后的样式
	 */
	public void setClickedResource() {
		this.setBackgroundResource(this.mClickedBackground);
	}
}
