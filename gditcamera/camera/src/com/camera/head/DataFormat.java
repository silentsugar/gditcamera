package com.camera.head;

/**
 * 数据转换工具类
 * @author 郑澍璋
 */
public class DataFormat {

	/**
	 * 整数(max=65535(默认))转换成长度为2(默认)的byte数组
	 * @param num
	 * @return
	 */
	public static byte[] int2bytes(int num) {
		int byteLen = 2;//如果数据长度值为4，则可以转换到最大整数值Integer.MAX_VALUE
		int offset = (byteLen-1)*8;
		
		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i<<3)));
		}
		return b;
	}
	
	/**
	 * 整数转换成长度为4的byte数组
	 * @param num
	 * @return
	 */
	public static byte[] int2bytesMax(int num) {
		int byteLen = 4;
		int offset = (byteLen-1)*8;
		
		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i<<3)));
		}
		return b;
	}
}
