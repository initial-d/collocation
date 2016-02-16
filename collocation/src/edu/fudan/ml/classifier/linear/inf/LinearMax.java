package edu.fudan.ml.classifier.linear.inf;

import edu.fudan.ml.classifier.Results;
import edu.fudan.ml.feature.Generator;
import edu.fudan.ml.types.HashSparseVector;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.SparseVector;

/**
 * @author xpqiu
 * @version 1.0
 */
public class LinearMax extends Inferencer {

	private static final long serialVersionUID = -7602321210007971450L;
	
	private Generator generator;
	private int ysize;

	public LinearMax(Generator generator, int ysize) {
		this.generator = generator;
		this.ysize = ysize;
	}
	
	public Results getBest(Instance inst)	{
		return getBest(inst, 1);
	}

	public Results getBest(Instance inst, int n) {

		Integer target = null;
		if (isUseTarget && inst.getTarget() != null)
			target = (Integer) inst.getTarget() ;
		
		Results<Integer> res = new Results<Integer>(n);
		if (target != null) {
			res.buildOracle();
		}

		for (int i = 0; i < ysize; i++) {
			HashSparseVector fv = generator.getVector(inst, i);
			float score = fv.dotProduct(weights);
			if (target != null && target == i)
				res.addOracle(score, i);
			else
				res.addPred(score, i);
		}
		return res;
	}
	
}
