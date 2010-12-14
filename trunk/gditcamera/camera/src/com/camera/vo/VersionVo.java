package com.camera.vo;

public class VersionVo {

	public static final String VERSION_NAME = "hband beta 1.0 版本";
	/** 版本一*/
	public static final int VERSION1 = 1;
	/** 版本二*/
	public static final int VERSION2 = 2;
	/** 标识当前版本*/
	public static int CURRENT_VERSION = VERSION2;
	
	public static final String PUBLIC_DATE = "2010年12月13日";
	
	public static final String UPLOAD_INFO = ""
			+ "1. 换了软件的图标\n" 
			+ "2. 新增非图片文件的浏览和上传功能\n"
			+ "3. 实现了服务器的双发功能，默认情况下将发送文件到默认的两个服务器上面，每次发送前会把文件切成两份切片，再进行发送，发送完第一个服务器后就接着发送给第二个服务器\n"
			+ "4. 优化图片的压缩和生成缩略图算法，提高压缩速度，且无论图片大小多少都可正常压缩\n"
			+ "5. 修改了服务器的提示信息，之前提示信息中的图片完整路径全部替换为图片名\n"
			+ "6. 图片上传界面新增一个区域提示文件名，每次点击后都会在此区域提示选中的图片名是什么\n"
			+ "7. 新增蓝牙扫描功能（此为新项目的一个功能模块，顺便也集合到项目里面）\n"
			+ "8. 实现了图片的断点续传功能，每次如果上传到一半失败了，下次再上传同样的图片，将会先检测该文件上次是否有未上传完的切片，如果检测到，将会给出提示，用户可以选择把上次未完成的上传任务上传完，或者直接删除上次未完成的切片，然后重新上传一次。\n"
			+ "9. 进行上传或退出程序的操作时，如果当时有任务正在上传，将会给出相应提示。\n"
			+ "10. 修改了程序的名称，且SDCARD中的程序目录名现改为hbang。\n"
			+ "11. 增加关于对话框，以后每个程序的修改及版本信息将会在此显示。\n";
	
}
