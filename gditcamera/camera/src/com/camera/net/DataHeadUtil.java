package com.camera.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.camera.util.Constant;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;
import com.camera.vo.DataHead;

/**
 * 把数据头信息转换成byte数组的工具
 * @author tian
 *
 */
public class DataHeadUtil { 
	
	private PreferencesDAO dao;
	private Context mContext;
	private Date lastTime;
	
	public DataHeadUtil(Context context){
		mContext = context;
	}
	
	/**
	 * 打印数组，测试用
	 * @param b
	 */
	public static void print(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i]);
			if (i < b.length - 1)
				System.out.print(",");
			else {
				System.out.println(" => " + bytes2int(b));
			}
		}
	}

	/**
	 * 长度为2(默认)的byte转换成整数
	 * @param b
	 * @return
	 */
	public static int bytes2int(byte[] b) {
		int byteLen = (b.length>=4 ? 4 : 2);
		int mask = 0xff;//1111 1111
		int temp = 0;
		int res = 0;
		
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
		int byteLen = 2;//如果数据长度值为4，则可以转换到最大整数值Integer.MAX_VALUE
		int offset = (byteLen-1)*8;
		
		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i<<3)));
		}
		return b;
	}
	
	/**
	 * 把数据包头实体类转换成byte数组
	 * @param dataHead
	 * @return
	 * @throws Exception
	 */
	public static byte[] dataHead2Byte(DataHead dataHead)throws Exception{
		byte [] result = new byte[90];
		int offset1 = 0;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String [] arr = dateFormat.format(dataHead.getDataTime()).split("-");
		int year1 = Integer.parseInt(arr[0].substring(0,2));
		int year2 = Integer.parseInt(arr[0].substring(2,4));
		int month = Integer.parseInt(arr[1]);
		int day = Integer.parseInt(arr[2]);
		int hour = Integer.parseInt(arr[3]);
		int minute = Integer.parseInt(arr[4]);
		int second = Integer.parseInt(arr[5]);
		
		byte[] tem;

		/**$PHO(4byte)*/
		byte [] phoByte = dataHead.getPho();
		for(int i=0;i<4;i++){
			result[offset1++] = phoByte[i];
		}
		/**分局名称(16byte)*/
		String subStation = dataHead.getSubStation();
		byte [] subStationByte = StringUtil.getByteArrayByLength(subStation, "GB2312", 16);
		for(int i=0;i<16;i++){
			result[offset1++] = subStationByte[i];
		}
		/**测站名称(16byte)*/
		String surveyStation = dataHead.getSurveyStation();
		byte [] surveyStationByte = StringUtil.getByteArrayByLength(surveyStation, "GB2312", 16);
		for(int i=0;i<16;i++){
			result[offset1++] = surveyStationByte[i];
		}
		/**照片描述,Unicode(32char=64byte)*/
		String phoDesc = dataHead.getPhoDesc();
//		Log.e("phoDesc", phoDesc.getBytes("GB2312").length+"");
		byte [] phoDescByte = StringUtil.getByteArrayByLength(phoDesc, "GB2312", 32);
		for(int i=0;i<32;i++){
			result[offset1++] = phoDescByte[i];
		}
		/**站码(8byte)*/
		String stationCode = dataHead.getStationCode();
		byte [] stationCodeByte =stationCode2BCDbytes(stationCode);
		for(int i=0;i<8;i++){
			result[offset1++] = stationCodeByte[i];
		}
		/**口令(16byte)*/
//		String command = dataHead.getCommand();
//		byte [] commandByte =StringUtil.getByteArrayByLength(command, "GB2312",16);
//		for(int i=0;i<16;i++){
//			result[offset1++] = commandByte[i];
//		}

		tem = int2bytes(year1);
		result[offset1++] = tem[1];
		
		tem = int2bytes(year2);
		result[offset1++] = tem[1];
		
		tem = int2bytes(month);
		result[offset1++] = tem[1];
		
		tem = int2bytes(day);
		result[offset1++] = tem[1];
		
		tem = int2bytes(hour);
		result[offset1++] = tem[1];
		
		tem = int2bytes(minute);
		result[offset1++] = tem[1];
		
		tem = int2bytes(second);
		result[offset1++] = tem[1];

		result[offset1++] = (byte)dataHead.getCameraId();

		tem = int2bytes(dataHead.getCurrentPackage());
		result[offset1++] = tem[0];
		result[offset1++] = tem[1];

		tem = int2bytes(dataHead.getTotalPackage());
		result[offset1++] = tem[0];
		result[offset1++] = tem[1];

		tem = int2bytes(dataHead.getDataLength());
		result[offset1++] = tem[0];
		result[offset1++] = tem[1];
		Log.e("10 : ", "10");
//		for(int i=0;i<result.length;i++){
//			Log.e("result["+i+"]=", result[i] + "");
//		}
//		Log.e("16 : ", "16");
//		for(int i=0;i<result.length;i++){
//			Log.e("result["+i+"]=", Integer.toHexString(result[i]));
//		}
		
		return result;
	}
	
	/**
	 * 把byte数组转换成数据包头实体类DataHead
	 */
	public static DataHead byte2DataHead(byte [] b){
		DataHead d = new DataHead();
		int offset = 0;
		try {
			Log.e("##################PHO=","");
			print(subByteArray(b,offset,4));
			d.setPho(subByteArray(b,offset,4));
			offset+=4;
			d.setSubStation(new String(subByteArray(b, offset, 16)));
			Log.d(offset+"dataHead:subStaticon", d.getSubStation());
			offset+=16;
			String s = new String(subByteArray(b, offset, 16));
			d.setSurveyStation(s);
			Log.d(offset+"dataHead:SurveyStation", d.getSurveyStation());
			offset+=16;
			d.setPhoDesc(new String(subByteArray(b, offset, 64),"GB2312"));
			Log.d(offset+"dataHead:Desc", d.getPhoDesc());
			offset+=64;
			d.setStationCode(new String(subByteArray(b, offset,8)));
			Log.d(offset+"dataHead:StationCode", d.getStationCode());
			offset+=8;
			d.setCommand(new String(subByteArray(b, offset,16)));
			Log.d(offset+"dataHead:Command", d.getCommand());
			offset+=16;
			String dataString = "";
//			print(subByteArray(b, offset, 7));
			for(int i=0;i<7;i++){
				if(i==0)
					dataString+=(b[offset++]);
				else
					dataString+=(b[offset++]+"#");
			}
			d.setDataTime(dataTimeString2Date(dataString));
			Log.d(offset+"dataHead:Date", new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(d.getDataTime()));
			d.setCameraId(b[offset]);
			Log.d(offset+"dataHead:CameraId", d.getCameraId()+"");
			offset+=1;
			d.setCurrentPackage(bytes2int(subByteArray(b, offset, 2)));
			Log.d(offset+"dataHead:CurrentPackage", d.getCurrentPackage()+"");
			offset+=2;
			d.setTotalPackage(bytes2int(subByteArray(b, offset, 2)));
			Log.d(offset+"dataHead:TotalPackage", d.getTotalPackage()+"");
			offset+=2;
			d.setDataLength(bytes2int(subByteArray(b, offset, 2)));
			Log.d(offset+"dataHead:dataLength", d.getDataLength()+"");

			return d;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte [] subByteArray(byte []b,int start,int length){
		if(length>b.length||start<0||(start+length)>b.length)
			return null;
		byte [] result = new byte[length];
		for(int i=0;i<length;i++){
			result[i] = b[start+i];
		}
		return result;
	}
	
	/**
	 * 时间字符串(20 10 11 03 20 37 05)转成日期对象
	 * @param dataTimeString
	 * @return
	 */
	public static Date dataTimeString2Date(String dataTimeString){
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy#MM#dd#HH#mm#ss#");
		Date d;
		try {
			d = dataFormat.parse(dataTimeString);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据传入的参数获取数据头对象
	 * @param desc 照片描述
	 * @param curr 当前包
	 * @param total 总包数
	 * @param len 数据长度
	 * @param useLastTime 是否使用上次获取数据包的时间
	 * @return
	 */
	public DataHead getHeadData(String desc,int curr,int total,int len,boolean useLastTime) {
		dao = new PreferencesDAO(this.mContext);
		DataHead dataHead = new DataHead();
//		byte [] bPho = new byte[]{0x24,0x50,0x48,0x4F};
//		dataHead.setPho(bPho);
//		dataHead.setSubStation("beijingshuiwen");
//		dataHead.setSurveyStation("ceshizhan");
//		dataHead.setPhoDesc("一号河流");
//		dataHead.setPhoDesc("desc");
//		dataHead.setStationCode("12345678");
//		dataHead.setCommand("1234567812345678");
//		dataHead.setDataTime(date);
//		dataHead.setCameraId((byte)1);
		dataHead.setPho(dao.getPreferences().getPho());
		dataHead.setSubStation(dao.getPreferencesByKey(Constant.STATION_SUB));
		dataHead.setSurveyStation(dao.getPreferencesByKey(Constant.STATION_SURVEY));
		dataHead.setPhoDesc(desc);
		dataHead.setStationCode(dao.getPreferencesByKey(Constant.STATION_CODE));
		dataHead.setCommand(dao.getPreferencesByKey(Constant.COMMAND));
		if(useLastTime){
			dataHead.setDataTime((lastTime==null?(lastTime=new Date()):lastTime));
		}else{
			dataHead.setDataTime(new Date());
		}
		dataHead.setCameraId((byte)1);
		dataHead.setCurrentPackage(curr);
		dataHead.setTotalPackage(total);
		dataHead.setDataLength(len);
		return dataHead;
	}
	
	public static byte [] getBytesHeadData(Context context, String desc,int curr,int total,int len,boolean useLastTime){
		try {
			DataHeadUtil dataHeadUtil = new DataHeadUtil(context);
			return dataHead2Byte(dataHeadUtil.getHeadData(desc, curr, total, len, useLastTime));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * BCD码转换成ASCII码
	 * @param bcd
	 * @return
	 */
	public static final byte BCD2Ascii(byte bcd){
		return 0;
	}
	
	/**
	 * ASCII码转换成BCD码
	 * 什么是BCD码
　　bcd码也叫8421码就是将十进制的数以8421的形式展开成二进制，大家知道十进制是0～9十个数组成，这十个数每个数都有自己的8421码：
　　0＝0000
　　1＝0001
　　2＝0010
　　3＝0011
　　4＝0100
　　5＝0101
　　6＝0110
　　7＝0111
　　8＝1000
　　9＝1001
　　举个例子：
　　321的8421码就是
　　3 2 1
　　0011 0010 0001
　　原因:0011=8x0+4x0+1x2+1x1=3 0010=8x0+4x0+2x1+1x0=2. 0001=8x0+4x0+2x0+1x1=1

	 * @param bcd BCD码
	 * @return
	 */
	public static final byte[] stationCode2BCDbytes(String stationCodes){
		//压缩
//		int dataLen = 8;
//		byte [] b = new byte[dataLen];
//		byte [] tmp = null;
//		if (stationCodes.length() % 2 != 0) {
//			stationCodes = "0" + stationCodes;
//		}
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		char[] cs = stationCodes.toCharArray();
//		for (int i = 0; i < cs.length; i += 2) {
//			int high = cs[i] - 48;
//			int low = cs[i + 1] - 48;
//			baos.write(high << 4 | low);
//		}
//		tmp = baos.toByteArray();
//		for(int i=0;i<dataLen;i++){
//			if(i<tmp.length){
//				b[i] = tmp[i];
//			}else{
//				b[i] = 0;
//			}
//		}
//		return b;
		//不压缩
		int dataLen = 8;
		byte [] b = new byte[dataLen];
		byte [] tmp = new byte[stationCodes.length()];
		int len = tmp.length;
		for(int i=0;i<len;i++){
			tmp[i] = (byte)Integer.parseInt(stationCodes.charAt(i)+"");
		}
		if(len==8){
			return tmp;
		}else{
			for(int i=0;i<dataLen;i++){
				if(i<len){
					b[i] = tmp[i];
				}else{
					b[i] = 0;
				}
			}
			return b;
		}
	}
}
