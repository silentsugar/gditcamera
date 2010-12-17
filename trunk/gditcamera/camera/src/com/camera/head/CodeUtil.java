package com.camera.head;

import java.io.UnsupportedEncodingException;

import android.util.Log;


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
	
	/**
	 * 反转BYTE数组
	 */
	public static byte[] reversalBytes(byte[] bytes) {
		byte tmp;
		for(int i = 0; i <= bytes.length / 2; i ++) {
			tmp = bytes[bytes.length - 1];
			bytes[bytes.length - i] = bytes[i];
			bytes[i] = tmp;
		}
		return bytes;
	}
	
	/**
	 * 把字符串按照特定的编码转换成特定长度的字节数组，字符串的字节数组超出指定的长度时返回null,少于指定长度补0
	 * @param str 要转换的字符串
	 * @param encoding 转换编码
	 * @param len 所要转成的byte数组的长度
	 * @return byte []
	 */
	public static final byte [] getGB2312ByteArray(String str){
		byte [] tmp;
		int tmpLen = 0;
		try {
			tmp = str.getBytes("GB2312");
			tmpLen = tmp.length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return tmp;
		
	}
	
}
