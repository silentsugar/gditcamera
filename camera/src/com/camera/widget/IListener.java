package com.camera.widget;

import java.util.List;

import android.view.View;

/**
 * 所有自定义的监听事件都放在该类中
 * @author 郑澍璋
 */
public interface IListener {

	/**
	 * 当一个容器中的某个控件被点击时，可使用该监听器
	 */
	public interface IOnItemClickListener {
		
		/**
		 * 【郑澍璋】单击监听事件
		 * @param list 窗口的控件列表
		 * @param view 被单击控件对象
		 * @param index 控件在容器中的位置
		 * @param id 被单击控件的ID
		 */
		public void onItemClick(List<View> list, View view, int index, int id);
	}
}
