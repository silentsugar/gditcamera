package com.camera.util;

/**
 * 字符串工具
 * @author yaotian
 *
 */
public class StringUtil {

	/**
	 * 检查分局名称是否合法
	 * @param subStation
	 * @return
	 */
	public static final boolean isCorrectSubStation(String subStation){
		if(subStation == null){
			return false;
		}
		if(subStation.length()>16 || subStation.length()<=0){
			return false;
		}
		char [] cArray = subStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查站码名称是否合法
	 * @param stationCode
	 * @return
	 */
	public static final boolean isCorrectStation(String stationCode){
		if(stationCode == null){
			return false;
		}
		if(stationCode.length()>8 || stationCode.length()<=0){
			return false;
		}
		char [] cArray = stationCode.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查分站名称是否合法
	 * @param surveyStation
	 * @return
	 */
	public static final boolean isCorrectSurveyStation(String surveyStation){
		if(surveyStation == null){
			return false;
		}
		if(surveyStation.length()>16 || surveyStation.length()<=0){
			return false;
		}
		char [] cArray = surveyStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return false;
			}
		}
		return true;
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
}
