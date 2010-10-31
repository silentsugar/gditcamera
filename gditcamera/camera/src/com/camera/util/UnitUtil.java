package com.camera.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 单位互转工具类
 * @author 郑澍璋
 */
public class UnitUtil {

	/**
	 * 把dip单位转成px单位
	 * @param context context对象
	 * @param dip dip数值
	 * @return
	 */
	public static int formatDipToPx(Context context, int dip) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		 return (int) Math.ceil( dip * dm.density);
	}
}
