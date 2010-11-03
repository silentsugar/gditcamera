package com.camera.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.camera.vo.Data;
import com.camera.vo.DataHead;

/**
 * 把数据头信息转换成byte数组的工具
 * @author tian
 *
 */
public class DataHeadUtil {
	
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

	public static int bytes2int(byte[] b) {
		int mask = 0xff;//1111 1111
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - (i<<3)));
//			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}
	

	public static byte[] dataHead2Byte(DataHead dataHead)throws Exception{
		byte [] result = new byte[122];
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
		StringBuffer fieldStr = new StringBuffer();
		//要按顺序
		fieldStr.append(dataHead.getPho());
		fieldStr.append(dataHead.getSubStation());
		fieldStr.append(dataHead.getSurveyStation());
		fieldStr.append(dataHead.getPhoDesc());
		fieldStr.append(dataHead.getStationCode());
		tem = fieldStr.toString().getBytes("GB2312");
		for(int i=0;i<tem.length;i++){
			result[i] = tem[i];
		}
		offset1 = tem.length;
		System.out.println(tem.length);

		tem = DataHeadUtil.int2bytes(year1);
		result[offset1++] = tem[3];
		
		tem = DataHeadUtil.int2bytes(year2);
		result[offset1++] = tem[3];
		
		tem = DataHeadUtil.int2bytes(month);
		result[offset1++] = tem[3];
		
		tem = DataHeadUtil.int2bytes(day);
		result[offset1++] = tem[3];
		
		tem = DataHeadUtil.int2bytes(hour);
		result[offset1++] = tem[3];
		
		tem = DataHeadUtil.int2bytes(minute);
		result[offset1++] = tem[3];
		
		tem = DataHeadUtil.int2bytes(second);
		result[offset1++] = tem[3];

		result[offset1++] = (byte)dataHead.getCameraId();

		tem = DataHeadUtil.int2bytes(dataHead.getCurrentPackage());
		result[offset1++] = tem[2];
		result[offset1++] = tem[3];

		tem = DataHeadUtil.int2bytes(dataHead.getTotalPackage());
		result[offset1++] = tem[2];
		result[offset1++] = tem[3];

		tem = DataHeadUtil.int2bytes(dataHead.getDataLength());
		result[offset1++] = tem[2];
		result[offset1++] = tem[3];

		return result;
	}
	
	public static DataHead byte2DataHead(byte [] b){
		DataHead d = new DataHead();
		int offset = 0;
		try {
			d.setPho(new String(subByteArray(b,offset,4),"GB2312"));
			offset+=4;
			d.setSubStation(new String(subByteArray(b, offset, 16)));
			offset+=16;
			String s = new String(subByteArray(b, offset, 16));
			d.setSurveyStation(s);
			offset+=16;
			d.setPhoDesc(new String(subByteArray(b, offset, 64),"GB2312"));
			offset+=64;
			d.setStationCode(new String(subByteArray(b, offset,8)));
			offset+=8;
			d.setDataTime(dataTimeString2Date(new String(subByteArray(b, offset, 7))));
			offset+=7;
			d.setCameraId(Byte.parseByte(new String(subByteArray(b, offset, 1))));
			offset+=1;
			d.setCurrentPackage(Byte.parseByte(new String(subByteArray(b, offset, 2))));
			offset+=2;
			d.setTotalPackage(Byte.parseByte(new String(subByteArray(b, offset, 2))));

			Log.d("dataHead:pho", d.getPho());
			Log.d("dataHead:subStation", d.getSubStation());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte [] subByteArray(byte []b,int start,int length){
		if(length>b.length||start<0||start>=length||(start+length)>b.length)
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
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
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
	 * 测试用
	 * @param args
	 * @throws Exception
	 */
//	public static void main(String args[])throws Exception{
//		DataHead data = new DataHead();
//		data.setPho("apho");
//		data.setSubStation("1234567890123456");
//		data.setSurveyStation("6543210987654321");
//		data.setPhoDesc("三十二个字符，六十四字节。三十二个字符，六十四字节，一二三四五六");
//		data.setStationCode("12345678");
//		data.setDataTime(new Date());
//		data.setCameraId((byte)1);
//		data.setCurrentPackage(22);
//		data.setTotalPackage(2049);
//		data.setDataLength(10000);
//		
//		print(DataHeadUtil.dataHead2Byte(data));
//	}
	
//	static void print(byte [] b){
//		for(int i=0;i<b.length;i++){
//			System.out.println("b["+i+"]="+b[i]+" ");
//		}
//	}
}
