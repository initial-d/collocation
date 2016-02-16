package edu.fudan.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
/**
 * 自定义文件操作类
 * @author xpqiu
 * @version 1.0
 * @since FudanNLP 1.5
 */
public class MyFiles {
	
	private static void allPath(List<File> files, File handle, String suffix) {
		if (handle.isFile()){
			if(suffix==null||handle.getName().endsWith(suffix))
				files.add(handle);
		}
		else if (handle.isDirectory()) {
			for (File sub : Arrays.asList(handle.listFiles()))
				allPath(files, sub,suffix);
		}
	}

	public static List<File> getAllFiles(String path, String suffix) {
		ArrayList<File> files = new ArrayList<File>();
		allPath(files, new File(path), suffix);
		return files;
	}

}
