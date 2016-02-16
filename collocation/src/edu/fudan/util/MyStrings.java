package edu.fudan.util;
/**
 * 自定义字符串操作类
 * @author xpqiu
 * @version 1.0
 * @since FudanNLP 1.5
 */
public class MyStrings {

	public static String normalizeRE(String string) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<string.length();i++){
			char c = string.charAt(i);
		}
		//([{\^-$|}])?*+
		return sb.toString();
	}
	/**
	 * 将字符串数组元素用空格隔开，返回字符串
	 * @param s 二维字符串数组
	 * @return 空格隔开的字符串
	 */
	public static String toString(String[][] s) {
		StringBuilder sb = new StringBuilder();

		for(int i=0;i<s.length;i++){
			for(int j=0;j<s[i].length;j++){
				sb.append(s[i][j]);
				if(j<s[i].length-1)
					sb.append(" ");
			}
			if(i<s.length-1)
				sb.append("\n");
		}
		return sb.toString();
	}
	/**
	 * 将字符串数组元素用空格隔开，返回字符串
	 * @param s 字符串数组
	 * @return 空格隔开的字符串
	 */
	public static String toString(String[] s) {
		StringBuilder sb = new StringBuilder();

		for(int i=0;i<s.length;i++){
			sb.append(s[i]);
			if(i<s.length-1)
				sb.append(" ");
		}
		return sb.toString();
	}
}
