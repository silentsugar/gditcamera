package com.camera.vo;

/**
 * 第二部分--图像信息
 * @author tian
 *
 */
public class DataImageInfo {

	private byte [] start = new byte[4];
	private int deviceCode;
	private int command;
	/**时间格式：年-月-日x时:分:秒*/
	private String time;
	/**单位名段*/
	private String unitName;
	
}
