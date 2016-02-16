package edu.fudan.ml.classifier;

import java.util.Arrays;


/**
 * 用来保存中间计算结果
 * pred为预测 oracle为真实
 * 
 * @author xpqiu
 * 
 */
public class Results<T> implements TResult {
	/**
	 * 记录前n个结果
	 */
	int n;
	/**
	 * 预测值得分
	 */
	public float[] predScores;
	/**
	 * 预测值
	 */
	public T[] predList;
	/**
	 * 真实值得分
	 */
	public float[] oracleScores;
	/**
	 * 真实值
	 */
	public T[] oracleList;

	public Object other;

	public Results() {
		this(1);
	}

	public Results(int n) {
		assert (n > 0);
		this.n = n;
		predScores = new float[n];
		predList = (T[]) new Object[n];
		Arrays.fill(predScores, Float.NEGATIVE_INFINITY);
	}

	/**
	 * 记录正确标注对应的得分
	 */
	public void buildOracle() {
		oracleScores = new float[n];
		oracleList = (T[]) new Object[n];
		Arrays.fill(oracleScores, Float.NEGATIVE_INFINITY);
	}

	/**
	 * 返回插入的位置
	 * 
	 * @param score 得分
	 * @param pred 预测值
	 * @return 插入位置
	 */
	public int addPred(float score, T pred) {
		return adjust(predScores, predList, score, pred);
	}

	/**
	 * 返回插入的位置
	 * 
	 * @param score 得分
	 * @param pred 预测值
	 * @return 插入位置
	 */
	public int addOracle(float score, T pred) {
		return adjust(oracleScores, oracleList, score, pred);
	}

	private int adjust(float[] scores, T[] preds, float score, T pred) {
		int i = 0;
		int ret = i;
		if (n != 0) {
			for (i = 0; i < n; i++) {
				if (score > scores[i])
					break;
			}
			if (i != n || n < scores.length) {
				for (int k = n - 2; k >= i; k--) {
					scores[k + 1] = scores[k];
					preds[k + 1] = preds[k];
				}
				ret = i;
			}else	 if (n < scores.length)	{

			}else	{
				ret = -1;
			}
		}
		if(ret!=-1){
			scores[i] = score;
		preds[i] = pred;
		}
		if (n < scores.length)
			n++;
		return ret;
	}

	/**
	 * 获得预测结果
	 * 
	 * @param i
	 *            位置
	 * @return 第i个预测结果；如果不存在，为NULL
	 */
	public T getPredAt(int i) {
		if (i < 0 || i >= n)
			return null;
		return predList[i];
	}

	/**
	 * 获得预测结果的得分
	 * 
	 * @param i
	 *            位置
	 * @return 第i个预测结果的得分；不存在为Double.NEGATIVE_INFINITY
	 */
	public float getScoreAt(int i) {
		if (i < 0 || i >= n)
			return Float.NEGATIVE_INFINITY;
		return predScores[i];
	}
	
	
	
	/**
	 * 预测结果数量
	 * 
	 * @return 预测结果的数量
	 */
	public int size() {
		return n;
	}

	@Override
	public void normalize() {
		float base = predScores[0]/2;
		float sum = 0;

		for(int i=0;i<predScores.length;i++){
			float s  = (float) Math.exp(predScores[i]/base);
			predScores[i] = s;
			sum +=s;
		}
		for(int i=0;i<predScores.length;i++){
			float s = predScores[i]/sum;
			predScores[i] = s;
			
		}
		
	}
}
