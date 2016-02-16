package edu.fudan.ml.feature;

import edu.fudan.ml.types.HashSparseVector;
import edu.fudan.ml.types.Instance;

/**
 * 简单将data返回 特征不包含类别信息
 * 
 * @author xpqiu
 * 
 */
public class BaseGenerator extends Generator {

	private static final long serialVersionUID = 5209575930740335391L;
	

	public HashSparseVector getVector(Instance inst) {

		return (HashSparseVector) inst.getData();
	}

	public HashSparseVector getVector(Instance inst, Object object) {
		return getVector(inst);
	}
}
