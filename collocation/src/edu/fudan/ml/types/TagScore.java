package edu.fudan.ml.types;

import java.util.List;
import java.util.Vector;

/**
 * KNN分类器结果记录
 * 
 * @author lcao
 *
 */

public class TagScore {

	/**
	 * 类标签
	 */
	private Object tag;
	
	/**
	 * 分数
	 */
	private double score;
	
	/**
	 * 出现次数
	 */
	private int times;
	
	/**
	 * 证据
	 */
	private List<String> source = new Vector<String>();
	
	public TagScore(Object t, double s){
		tag = t; 
		score = s;
		times = 1;
	}
	
	public TagScore(Object t, double s, String sou){
		tag = t; 
		score = s;
		times = 1;
		source.add(sou);
	}
	
	public String toString(){
		String s = "分类结果:" + tag + "\n分数: " + score + "\n证据：";
		for(int i = 0; i < source.size(); i++)
			s += source.get(i) + "\n";
		return s;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public Object getTag() {
		return tag;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getTimes() {
		return times;
	}
	
	public List<String> getSource(){
		return source;
	}
	
	public void setSource(List<String> source){
		this.source = source;
	}
	
	public void addSource(String s){
		source.add(s);
	}
	
	public void addAllSource(List<String> source){
		this.source.addAll(source);
	}
}
