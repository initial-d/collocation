package edu.fudan.ml.feature;

import edu.fudan.ml.feature.Generator;
import edu.fudan.ml.types.HashSparseVector;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.SparseVector;

/**
 * 结构化特征生成类
 * 
 * @version Feb 16, 2009
 */
public class SFGenerator extends Generator	{

	private static final long serialVersionUID = 6404015214630864081L;

	/**
	 * 构造函数
	 */
	public SFGenerator() {
	}

	@Override
	public HashSparseVector getVector(Instance inst, Object label) {
		int[] data = (int[]) inst.getData();
		HashSparseVector fv = new HashSparseVector();
		for(int i = 0; i < data.length; i++)	{
			int idx = data[i]+(Integer)label;
			fv.add(idx, 1.0f);
		}
		return fv;
	}
}
