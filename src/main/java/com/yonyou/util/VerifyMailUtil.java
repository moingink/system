package com.yonyou.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用正则表达式来验证邮箱格式或网址格式是否正确
 * 
 * @author yansu
 *
 */
public class VerifyMailUtil {

	/**
	 * @param 待验证的字符串
	 * @return 如果是符合邮箱格式的字符串,返回<b>true</b>,否则为<b>false</b>
	 */
	public static boolean isEmail(String str) {
		String[] strarr=str.split("@");
		if (strarr.length>1) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @param 待验证的字符串
	 * @return 如果是符合邮箱格式的字符串,返回<b>true</b>,否则为<b>false</b>
	 */
//	public static boolean isEmail1(String str) {
//		String check=("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$");
//		if (strarr.length>1) {
//			return true;
//		}else {
//			return false;
//		}
//	}
	
	public static boolean checkEmail(String email) {
		String check = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static void main(String[] args) {
		System.out.println(isEmail("ya@nsu_1qq.com"));
		//System.out.println(checkEmail("123@Qqcom"));
	}
}
