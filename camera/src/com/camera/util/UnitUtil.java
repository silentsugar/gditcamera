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
		 return  (int)Math.ceil( dip * dm.density);
	}
	
	/**
	 * 把dip单位转成px单位
	 * @param context context对象
	 * @param dip dip数值
	 * @return
	 */
	public static double formatDipToPx(Context context, double dip) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		 return  Math.ceil( dip * dm.density);
	}
	
	/**
	 * 把dip单位转成px单位
	 * @param context context对象
	 * @param dips dip数组
	 * @return 返回px结果数组
	 */
	public static int[] formatDipToPx(Context context, int[] dips) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
	     int[] result = new int[dips.length];
	     for(int i = 0; i < dips.length; i ++) {
	    	 result[i] =  (int)Math.ceil( dips[i] * dm.density);
	     }
		 return result;
	}
	
	/**
	 * 把dip单位转成px单位
	 * @param context context对象
	 * @param dips dip数组
	 * @return 返回px结果数组
	 */
	public static double[] formatDipToPx(Context context, double[] dips) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
	     double[] result = new double[dips.length];
	     for(int i = 0; i < dips.length; i ++) {
	    	 result[i] =  Math.ceil( dips[i] * dm.density);
	     }
		 return result;
	}
}
