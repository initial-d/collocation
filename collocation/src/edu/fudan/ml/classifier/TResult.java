package edu.fudan.ml.classifier;

/**
 * 结果文件接口
 * @author xpqiu
 *
 */
public interface TResult<T> {
	
	public T getPredAt(int i);
	public float getScoreAt(int i);
	public void normalize();
	

}
