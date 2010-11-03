package com.camera.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.activity.R;
import com.camera.util.UnitUtil;

/**
 * 顶部标题栏控件
 * @author 郑澍璋
 */
public class CTitleBar extends RelativeLayout {

	//左边按钮
	private View mLeftView;
	//右边按钮
	private View mRightView;
	//中间TextView标题
	private View mTitleView;
	//Context对象
	private Context context;
	
	private int mLeftViewId, mRightViewId, mTitleViewId;
	
	public CTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CTitleBar);
		System.out.println("left_view: " + a.getString(R.styleable.CTitleBar_left_view));
		System.out.println("right_view: " + a.getString(R.styleable.CTitleBar_right_view));
		mLeftViewId = a.getResourceId(R.styleable.CTitleBar_left_view, 0);
		mRightViewId = a.getResourceId(R.styleable.CTitleBar_right_view, 0);
		mTitleViewId = a.getResourceId(R.styleable.CTitleBar_title_view, 0);
		a.recycle();
	}
	
	public CTitleBar(Context context) {
		super(context);
		this.context = context;
	}
	
	/**
	 * 标题栏的构造函数
	 * @param txtTitle 标题控件
	 * @param leftView 左边控件
	 * @param rightView 右边控件
	 */
	public CTitleBar(Context context, TextView txtTitle, 
			TextView leftView, TextView rightView) {
		super(context);
		this.context = context;
		this.addLeftView(leftView);
		this.addRightView(rightView);
		this.addTitleView(txtTitle);
	}
	
	/**
	 * 加载默认的标题栏三个控件，分别是左边控件，中间TextView标题，右边控件,
	 * 如果其中任一参数为NULL，则表示不加载该控件
	 * @param title 标题
	 * @param txtBtnLeft 左边按钮Text
	 * @param txtBtnRight 右边按钮Text
	 */
	public void loadDefaulComponents(String title, String txtBtnLeft, String txtBtnRight) {
		LayoutParams lp;
		//加入标题TextView控件
		if(title != null) {
			lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			int i = UnitUtil.formatDipToPx(context, 100);
			lp.setMargins(i, 0, i, 0);
			TextView txtTitle = new TextView(this.getContext());
			txtTitle.setGravity(Gravity.CENTER);
			txtTitle.setText(title);
			txtTitle.setTextSize(20);
			txtTitle.setTextColor(Color.WHITE);
			this.mTitleView = txtTitle;
			this.addView(mTitleView, lp);
		}
		//加入左边的Button控件
		if(txtBtnLeft != null) {
			lp = new LayoutParams(UnitUtil.formatDipToPx(context, 50), LayoutParams.FILL_PARENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.alignWithParent = true;
			int i = UnitUtil.formatDipToPx(context, 5);
			lp.setMargins(i, i, i, i);
			mLeftView = new Button(this.getContext());
			((TextView)mLeftView).setText(txtBtnLeft);
			this.addView(mLeftView, lp);
		}
		//加入右边的Button控件
		if(txtBtnRight != null) {
			lp = new LayoutParams(UnitUtil.formatDipToPx(context, 50), LayoutParams.FILL_PARENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.alignWithParent = true;
			int i = UnitUtil.formatDipToPx(context, 5);
			lp.setMargins(i, i, i, i);
			mRightView = new Button(this.getContext());
			((TextView)mRightView).setText(txtBtnLeft);
			this.addView(mRightView, lp);
		}
	}

	/**
	 * 获取标题栏左边控件
	 */
	public View getLeftView() {
		return mLeftView;
	}

	/**
	 * 设置标题栏的左边控件
	 */
	public void addLeftView(View mLeftView) {
		addLeftView(mLeftView, null);
	}
	
	/**
	 * 设置标题栏的左边控件
	 */
	public void addLeftView(View mLeftView, LayoutParams lp) {
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		}
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.alignWithParent = true;
		int i = UnitUtil.formatDipToPx(context, 5);
		lp.setMargins(i, i, i, i);
		mLeftView.setPadding(i + 2, mLeftView.getPaddingTop(), i + 2, mLeftView.getPaddingBottom());
		super.addView(mLeftView, -1, lp);
		this.mLeftView = mLeftView;
	}

	/**
	 * 获取标题栏右边控件
	 */
	public View getRightView() {
		return mRightView;
	}

	/**
	 * 设置标题栏的右边控件
	 */
	public void addRightView(View mRightView, LayoutParams lp) {
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		}
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.alignWithParent = true;
		int i = UnitUtil.formatDipToPx(context, 5);
		lp.setMargins(i, i, i, i);
		mRightView.setPadding(i + 2, mRightView.getPaddingTop(), i + 2, mRightView.getPaddingBottom());
		super.addView(mRightView, -1, lp);
		this.mRightView = mRightView;
	}
	
	/**
	 * 设置标题栏的右边控件
	 */
	public void addRightView(View mRightView) {
		addRightView(mRightView, null);
	}

	/**
	 * 获取标题栏中间控件
	 */
	public View getTitleView() {
		return mTitleView;
	}

	/**
	 * 设置标题栏的中间控件
	 * @param mTxtTitle
	 */
	public void addTitleView(View mTxtTitle) {
		addTitleView(mTxtTitle, null);
	}
	
	/**
	 * 设置标题栏的中间控件
	 * @param mTxtTitle
	 */
	public void addTitleView(View mTxtTitle, LayoutParams lp) {

		
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		}
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.alignWithParent = true;
		int i = UnitUtil.formatDipToPx(context, 5);
		lp.setMargins(0, i, 0, i);
		mTxtTitle.setPadding(i + 2, mTxtTitle.getPaddingTop(), i + 2, mTxtTitle.getPaddingBottom());
		super.addView(mTxtTitle, -1, lp);
		this.mTitleView = mTxtTitle;
	}

	/**
	 * 重写addViewInLayout函数，当增加的控件为标题，左控件或右控件时，重新导航到自定义的函数中
	 */
	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		int id = child.getId();
		if (id == mLeftViewId) {
			this.addLeftView(child);
			return;
		} else  if (id == mRightViewId) {
			this.addRightView(child);
			return;
		} else if (id == mTitleViewId) {
			this.addTitleView(child);
			return;
		}
		super.addView(child, index, params);
	}
	
	
	
	
}
