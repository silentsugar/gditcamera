package com.camera.vo;

import java.util.List;

/**
 * 配置信息实体
 * @author yaotian
 *
 */
public class Preferences {

	/**默认到图片目录*/
	private String defaultImgDir;
	/**分站名称(16byte)*/
	private String subStation;
	/**测站名称(16byte)*/
	private String surveyStation;
	/**站码(8byte)*/
	private String stationCode;
	/**服务器地址列表，格式:http://192.168.1.1:8080或http://www.baidu.com:8080,默认使用80端口*/
	private List<String> hostList;
	
}
