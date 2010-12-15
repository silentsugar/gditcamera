package com.camera.head;


public class CodeUtil {

	/**
	 * 的byte数组转换成整数
	 * @param b
	 * @return
	 */
	public static long bytes2int(byte[] b) {
		int byteLen = (b.length >= 4 ? 4 : 2);
		long mask = 0xff;
		long temp = 0;
		long res = 0;

		for (int i = 0; i < byteLen; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	/**
	 * 整数(max=65535(默认))转换成长度为2(默认)的byte数组
	 * @param num
	 * @return
	 */
	public static byte[] int2bytes(int num) {
		int byteLen = 2;// 如果数据长度值为4，则可以转换到最大整数值Integer.MAX_VALUE
		int offset = (byteLen - 1) * 8;

		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i << 3)));
		}
		return b;
	}
	
	/**
	 * 整数转换成长度为4的byte数组
	 * @param num
	 * @return
	 */
	public static byte[] int2bytesMax(long num) {
		int byteLen = 4;
		int offset = (byteLen - 1) * 8;

		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i << 3)));
		}
		return b;
	}
	
	/**
	 * byte数组转换成字符串
	 * @param bytes byte数组
	 * @return 字符串
	 */
	public static String bytes2String(byte[] bytes) {
		String str = "";
		int n = bytes.length;
		for(int i = 0; i < n; i ++) {
			str += (char)(bytes[i]);
		}
		return str;
	}
	
}
