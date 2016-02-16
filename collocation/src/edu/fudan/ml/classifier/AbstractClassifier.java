/*
 * 文件名：Classifier.java
 * 版权：Copyright 2008-20012 复旦大学 All Rights Reserved.
 * 修改人：xpqiu
 * 修改时间：2009 Sep 6, 2009 11:09:40 AM
 * 修改内容：新增
 *
 * 修改人：〈修改人〉
 * 修改时间：YYYY-MM-DD
 * 修改内容：〈修改内容〉
 */
package edu.fudan.ml.classifier;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;

/**
 * 分类器抽象类
 * @author xpqiu
 * @version 1.0
 * Classifier
 * package edu.fudan.ml.classifier
 */
public abstract class AbstractClassifier{

	public abstract TResult predict(Instance instance);
	public abstract TResult predict(Instance instance,int nbest);

	public abstract int classify(Instance instance);
	

	public TResult[] classify(InstanceSet set,int nbest) {
		TResult[] res= new TResult[set.size()];
		for(int i=0;i<set.size();i++){
			res[i]=  classify(set.getInstance(i),nbest);			
		}
		return res;
	}
	
	private TResult classify(Instance instance, int nbest) {
		return  predict(instance,nbest);
	}

	public Integer[] classify(InstanceSet set) {
		Integer[] pred= new Integer[set.size()];
		for(int i=0;i<set.size();i++){
			pred[i]=  (Integer) classify(set.getInstance(i));			
		}
		return pred;
	}

}
