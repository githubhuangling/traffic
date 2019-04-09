package com.ctf.traffic.util;

import com.ctf.traffic.remote.RemoteInvoke;

public class SerialNumberUtil {
	private static final String[] ATTACH = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static String parseToBase64(String serialNumber) {
		String[] sn = RemoteInvoke.base64Encode(serialNumber).split("");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<sn.length-2;i++) {//每隔一位加一个随机字符
			sb.append(sn[i]);
			//sb.append(ATTACH[new Random().nextInt(ATTACH.length)]);
			sb.append(ATTACH[sn[i].charAt(0)%ATTACH.length]);
		}
		sb.append(sn[sn.length-2]);
		sb.append(sn[sn.length-1]);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(SerialNumberUtil.parseToBase64("1351531810656845"));
	}
}
