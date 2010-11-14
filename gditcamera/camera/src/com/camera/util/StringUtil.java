package com.camera.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.util.Log;

/**
 * 字符串工具类
 * @author 郑澍璋
 */
public class StringUtil {

	/**
	 * 把文件路径的"/"号替换成"_",如/mnt/abc/将转换成mnt_abc_
	 * @param filePath 文件夹
	 * @return 转换后的字符串
	 */
	public static String convertFolderPath(String filePath) {
		if(filePath.charAt(0) == '/')
			filePath = filePath.substring(1);
		return filePath.replace("/", "_");
	}
	
	/**
	 * 把缩略图片路径转换成原图片路径
	 * @param filePath 缩略图路径
	 * @return
	 */
	public static String convertBackFolderPath(String filePath) {
		int position = filePath.lastIndexOf("/");
		String strTmp = filePath.substring(position);
		return strTmp.replace("_", "/");
	}
	
	/**
	 * 检查图片路径是否合法
	 * @param imgDir
	 * @return
	 */
	public static final String isCorrectImgDir(String imgDir){
		if(imgDir.indexOf("/")!=0){
			return "路径要以\"/\"开头";
		}
		File f = new File(imgDir);
		if((!f.exists())){
			return "该文件夹不存在";
		}
		if(!f.isDirectory()){
			return "不是目录";
		}
		return null;
	}
	/**
	 * 检查分局名称是否合法
	 * @param subStation
	 * @return 错误信息
	 */
	public static final String isCorrectSubStation(String subStation){
		if(subStation == null || subStation.length()<=0){
			return "不允许为空";
		}
		if(subStation.length()>16){
			return "不能超过16个字符";
		}
		char [] cArray = subStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "请输入数字或英文字母";
			}
		}
		return null;
	}
	
	/**
	 * 检查站码是否合法
	 * @param stationCode
	 * @return
	 */
	public static final String isCorrectStationCode(String stationCode){
		if(stationCode == null || stationCode.length()<=0){
			return "不允许为空";
		}
		if(stationCode.length()>8){
			return "不能超过8个字符";
		}
		char [] cArray = stationCode.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "请输入数字或英文字母";
			}
		}
		return null;
	}
	
	/**
	 * 检查分站名称是否合法
	 * @param surveyStation
	 * @return
	 */
	public static final String isCorrectSurveyStation(String surveyStation){
		if(surveyStation == null || surveyStation.length()<=0){
			return "不允许为空";
		}
		if(surveyStation.length()>16){
			return "不能超过16个字符";
		}
		char [] cArray = surveyStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "请输入数字或英文字母";
			}
		}
		return null;
	}
	
	/**
	 * 通过ASCII码判断 char 是否是大小写字母、或者数字中的某一个
	 * @param char c
	 * @return boolean
	 */
	public static final boolean isValidateChar(char c){
		int asciiCode = (int)c;
		if( (asciiCode>=48&&asciiCode<=57) || (asciiCode>=65&&asciiCode<=90) || (asciiCode>=97&&asciiCode<=122)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 把字符串按照特定的编码转换成特定长度的字节数组，字符串的字节数组超出指定的长度时返回null,少于指定长度补0
	 * @param str 要转换的字符串
	 * @param encoding 转换编码
	 * @param len 所要转成的byte数组的长度
	 * @return byte []
	 */
	public static final byte [] getByteArrayByLength(String str,String encoding,int len){
		byte [] b = new byte[len];
		byte [] tmp;
		int tmpLen = 0;
		try {
			tmp = str.getBytes(encoding);
			tmpLen = tmp.length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		if(tmpLen>len){ 
			Log.e("String", "the String:("+str+") bytes array length("+tmpLen+") is Longer then "+len);
			return null;
		}
		for(int i=0;i<len;i++){
			if(i>=tmpLen)
				b[i] = 0;
			else
				b[i] = tmp[i];
		}
		return b;
		
	}
}
