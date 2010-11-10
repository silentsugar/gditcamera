package com.camera.util;

/**
 * 存放常量值
 * @author tian
 *
 */
public class Constant {

	/**每个包头大小*/
	public static final int DATA_HEAD_SIZE = 122;
	/**每个数据包大小*/
	public static final int PACKAGE_SIZE = 1024;
	
	/** 应用程序目录*/
	public static final String APP_FOLDER = "camera/";
	/** 应用程序图片切片目录*/
	public static final String PIECE_FOLDER = "camera/wUpload";
	
	/**配置文件里面的属性名：图片目录*/
	public static final String IMAGE_DIR = "ImgDir";
	/**配置文件里面的属性名：服务器主机地址列表(如：主机1，配置文件里的属性名为host_1)*/
	public static final String HOST_LIST = "host_";
	/**配置文件里面的属性名：站码*/
	public static final String STATION_CODE = "stationCode";
	/**配置文件里面的属性名：分站*/
	public static final String STATION_SUB = "subStation";
	/**配置文件里面的属性名：测站*/
	public static final String STATION_SURVEY = "surveyStation";
	
}
