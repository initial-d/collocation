package edu.fudan.ml.types;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Dictionary {
	private int MAX_LEN = 7;
	private int MIN_LEN = 2;
	private TreeSet<String> dict = new TreeSet<String>();
	private TreeMap<String, String[]> dictPOS = new TreeMap<String, String[]>();
	private TreeMap<String, int[]> index = new TreeMap<String, int[]>();
	private int indexLen = 2;
	private boolean isAmbiguity = false;
	
	public static ArrayList<String[]> format(ArrayList<String> al) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for(String s: al) {
			list.add(new String[]{s});
		}
		return list;
	}
	
	public Dictionary(String path, boolean isAmbiguity) throws FileNotFoundException {
		this.setAmbiguity(isAmbiguity);
		createDict(path);
		indexLen = MIN_LEN;
		createIndex();
	}
	
	public Dictionary(ArrayList<String[]> al, boolean isAmbiguity) {
		this.setAmbiguity(isAmbiguity);
		createDict(al);
		indexLen = MIN_LEN;
		createIndex();
	}
	
	public Dictionary(String path) throws FileNotFoundException {
		this.setAmbiguity(false);
		createDict(path);
		indexLen = MIN_LEN;
		createIndex();
	}
	
	public Dictionary(ArrayList<String[]> al) {
		this.setAmbiguity(false);
		createDict(al);
		indexLen = MIN_LEN;
		createIndex();
	}
	
	private void createDict(String path) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(path), "utf-8");
		ArrayList<String[]> al = new ArrayList<String[]>();
		while(scanner.hasNext()) {
			String line = scanner.nextLine().trim();
			if(line.length() > 0) {
				String[] s = line.split("\\s");
				al.add(s);
			}
		}
		createDict(al); 
	}
	
	private void createDict(ArrayList<String[]> al) {
		MAX_LEN = Integer.MIN_VALUE;
		MIN_LEN = Integer.MAX_VALUE;
		TreeMap<String, TreeSet<String>> dp = new TreeMap<String, TreeSet<String>>();
		for(int i = 0; i < al.size(); i++) {
			String[] s = al.get(i);
			if(s[0].length() > MAX_LEN)
				MAX_LEN = s[0].length();
			if(s[0].length() < MIN_LEN)
				MIN_LEN = s[0].length();
			dict.add(s[0]);
			for(int j = 1; j < s.length; j++) {
				if(dp.containsKey(s[0]) == false) {
					TreeSet<String> set = new TreeSet<String>();
					set.add(s[j]);
					dp.put(s[0], set);
				} else {
					dp.get(s[0]).add(s[j]);
				}
			}
		}
		if(dp.size() > 0)
			for(Entry<String, TreeSet<String>> entry: dp.entrySet()) {
				String key = entry.getKey();
				TreeSet<String> set = entry.getValue();
				String[] sa = new String[set.size()];
				set.toArray(sa);		
				dictPOS.put(key, sa);
			}
		
//		for(Entry<String, String[]> entry: dictPOS.entrySet()) {
//			String key = entry.getKey();
//			String[] set = entry.getValue();
//			System.out.print(key);
//			for(int i = 0; i < set.length; i++)
//				System.out.print("/" + set[i]);
//			System.out.println();
//		}
	}
	
	private void createIndex() {
//		System.out.println("indexLen: " + indexLen);
		
		TreeMap<String, TreeSet<Integer>> indexT = new TreeMap<String, TreeSet<Integer>>();
		for(String s: dict) {
			if(s.length() < indexLen)
				continue;
			String temp = s.substring(0, indexLen);
			//System.out.println(temp);
			if(indexT.containsKey(temp) == false) {
				TreeSet<Integer> set = new TreeSet<Integer>();
				set.add(s.length());
				indexT.put(temp, set);
			} else {
				indexT.get(temp).add(s.length());
			}
		}
		for(Entry<String, TreeSet<Integer>> entry: indexT.entrySet()) {
			String key = entry.getKey();
			TreeSet<Integer> set = entry.getValue();
			int[] ia = new int[set.size()];
			int i = set.size();
//			System.out.println(key);
			for(Integer integer: set) {
				ia[--i] = integer;
				
			}
//			for(int j = 0; j < ia.length; j++) 
//				System.out.println(ia[j]);
			
			index.put(key, ia);
		}
//		System.out.println(indexT);
	}
	
	public int getMaxLen() {
		return MAX_LEN;
	}
	
	public int getMinLen() {
		return MIN_LEN;
	}
	
	public boolean contains(String s) {
		return dict.contains(s);
	}
	
	public int[] getIndex(String s) {
		return index.get(s);
	}
	
	public String[] getPOS(String s) {
		return dictPOS.get(s);
	}
	
	public int getDictSize() {
		return dict.size();
	}
	
	public int getIndexLen() {
		return indexLen;
	}

	public boolean isAmbiguity() {
		return isAmbiguity;
	}

	private void setAmbiguity(boolean isAmbiguity) {
		this.isAmbiguity = isAmbiguity;
	}
	
	public TreeSet<String> getDict() {
		return dict;
	}
	public TreeMap<String, String[]> getPOSDict() {
		return dictPOS;
	}
	
	public TreeMap<String, int[]> getIndex() {
		return index;
	}
}
