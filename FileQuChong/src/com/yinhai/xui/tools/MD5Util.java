package com.yinhai.xui.tools;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MD5Util {
	// cache所有求过md5的内容
	private static Map map = Collections.synchronizedMap(new HashMap());

	public static void destroy() {
		if (null != map)
			map.clear();
		map = null;
	}

	public static String MD5(String s) {
		if (null != map.get(s))
			return map.get(s).toString();
		String s1 = MD5(s.getBytes());// "" + (map.size() + 1);
		map.put(s, s1);
		return s1;
	}

	public final static String MD5(byte[] btInput) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			// MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// mdInst.update(btInput);
			// byte[] md = mdInst.digest();
			// int j = md.length;
			// char str[] = new char[j * 2];
			// int k = 0;
			// for (int i = 0; i < j; i++) {
			// byte byte0 = md[i];
			// str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			// str[k++] = hexDigits[byte0 & 0xf];
			// }
			// return new String(str);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(btInput);
			return bytes2Hex(md5.digest());
		} catch (Exception e) {
			// if(org.apache.commons.logging.LogFactory.getLog(this.getClass()).isDebugEnabled())e.printStackTrace();
			return null;
		}
	}

	// 将字节数组转换成16进制的字符串
	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public static void main(String[] args) {
		System.out.print(MD5Util.MD5("AusITcim#1485"));
	}
}