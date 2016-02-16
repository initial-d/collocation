package edu.fudan.data.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.fudan.ml.types.Instance;

/**
 * @author xpqiu
 * @version 1.0
 * SimpleFileReader
 * 绠�鍗曟枃浠舵牸寮忓涓嬶細
 * 绫诲埆 锛� 鈥滃垎闅旂鈥� 锛� 鏁版嵁 
 * 鎴栬��
 * 鏁版嵁 锛� 鈥滃垎闅旂鈥� 锛� 绫诲埆
 * 
 * package edu.fudan.ml.data
 */
public class SimpleFileReader extends Reader {
	
	public enum Type{
		LabelData,  //绫诲埆 锛� 鈥滃垎闅旂鈥� 锛� 鏁版嵁
		DataLabel  //鏁版嵁 锛� 鈥滃垎闅旂鈥� 锛� 绫诲埆
	}

	String content = null;
	/**
	 * 绫诲埆鍜屾暟鎹箣闂寸殑榛樿鍒嗗壊绗︿负锛氱┖鏍�
	 */
	String sep = " ";
	BufferedReader reader;
	int line;
	private boolean isSplited=false;
	/**
	 * 鏁版嵁鏍煎紡绫诲瀷
	 */
	private Type type = Type.LabelData;

	/**
	 * 鏁版嵁璺緞
	 * @param file
	 */
	public SimpleFileReader(String file){
		init(file);
	}

	public SimpleFileReader(String c, boolean b, String test){
		content = c;
		isSplited = b;
	}
	
	/**
	 * 
	 * @param file 鏁版嵁璺緞
	 * @param b 鏄惁浠ョ┖鏍煎垎闅旀暟鎹�
	 */
	public SimpleFileReader(String file, boolean b) {
		init(file);
		isSplited = b;
	}
	/**
	 * 鑷畾涔夊垎闅旂
	 * @param file 鏁版嵁璺緞
	 * @param s 鑷畾涔夊垎闅旂
	 * @param b 鏄惁浠ョ┖鏍煎垎闅旀暟鎹�
	 * @param o 鏁版嵁鏍煎紡绫诲瀷
	 */
	public SimpleFileReader(String file, String s,boolean b,Type t) {
		init(file);
		sep = s;
		isSplited = b;
		type = t;
	}


	private void init(String file) {
		try {
			File f = new File(file);
			FileInputStream in = new FileInputStream(f);
			reader = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		line=0;
	}


	public boolean hasNext() {
		
		try {
			while(true){
			content = reader.readLine();
			line++;
			if(content==null){
				reader.close();
				return false;
			}
			//璺宠繃绌鸿
			if(content.trim().length()>0)
				break;				
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	public Instance next() {
		
		String data;
		String label;
		if(type == Type.LabelData){
			int idx = content.indexOf(sep);
			if(idx==-1){
				System.err.println("SimpleFileReader 鏁版嵁鏍煎紡涓嶅");
				System.err.println(line+"琛�: "+content);
				return null;
			}
			data = content.substring(idx+sep.length()).trim();
			label = content.substring(0, idx);
		}else{
			int idx = content.lastIndexOf(sep);
			if(idx==-1){
				System.err.println("SimpleFileReader 鏁版嵁鏍煎紡涓嶅");
				System.err.println(line+"琛�: "+content);
				return null;
			}
			label = content.substring(idx+sep.length()).trim();
			data = content.substring(0, idx);
		}
		if(data.length()==0){
			System.err.println("SimpleFileReader 鏁版嵁涓虹┖瀛楃涓�");
			System.err.println(line+"琛�: "+content);
			return null;
		}
		if(isSplited){
			String[] tokens = data.split("\\t+|\\s+");
			List<String> newdata = Arrays.asList(tokens);
			return new Instance (newdata,label);
		}else
			return new Instance (data, label);
	}

}
