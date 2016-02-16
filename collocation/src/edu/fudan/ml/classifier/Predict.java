package edu.fudan.ml.classifier;

/**
 * 用来输出带得分的预测结果
 * @author xpqiu
 *
 */
public class Predict {
	public String[] label;
	public float[] score;
	
	public Predict(int n){
		label = new String[n];
		score = new float[n];
	}

	public void set(int i, String label2, float d) {
		label[i] = label2;
		score[i] = d;
		
	}

	public int size() {		
		return label.length;
	}

}
