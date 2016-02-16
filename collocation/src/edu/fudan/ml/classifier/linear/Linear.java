package edu.fudan.ml.classifier.linear;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.fudan.ml.classifier.AbstractClassifier;
import edu.fudan.ml.classifier.Results;
import edu.fudan.ml.classifier.TResult;
import edu.fudan.ml.classifier.linear.inf.Inferencer;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.Instance;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.util.exception.LoadModelException;

/**
 * 线性分类器
 * 
 * @author xpqiu
 * 
 */
public class Linear extends AbstractClassifier implements Serializable	{

	private static final long serialVersionUID = -2626247109469506636L;

	protected Inferencer inferencer;
	protected AlphabetFactory factory;
	protected Pipe pipe;

	public Linear(Inferencer inferencer, AlphabetFactory factory) {
		this.inferencer = inferencer;
		this.factory = factory;
	}

	public Linear() {		
	}

	public Results predict(Instance instance) {

		Results pred = (Results) inferencer.getBest(instance);

		return pred;
	}

	public Results predict(Instance instance, int n) {
		return (Results) inferencer.getBest(instance, n);
	}
	
	/**
	 * 得到类标签
	 * @param instance 待分类样本
	 * @return
	 */
	public String getLabel(Instance instance) {
		Results res = (Results) inferencer.getBest(instance, 1);
		int idx =  (Integer) res.getPredAt(0);
		return factory.DefaultLabelAlphabet().lookupString(idx);
	}
	
	/**
	 * 得到分类结果
	 * @param instance 待分类样本
	 * @return 类标签对应的索引
	 */
	public int classify(Instance instance) {
		Results res = (Results) inferencer.getBest(instance, 1);
		int idx =  (Integer) res.getPredAt(0);
		return idx;
	}
	
	/**
	 * 得到类标签
	 * @param idx 数字型的分类结果，类标签对应的索引
	 * @return
	 */
	public String getLabel(int idx) {
		return factory.DefaultLabelAlphabet().lookupString(idx);
	}

	
	public void saveTo(String file) throws IOException {
		File f = new File(file);
		File path = f.getParentFile();
		if(!path.exists()){
			path.mkdirs();
		}
		
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(
				new BufferedOutputStream(new FileOutputStream(file))));
		out.writeObject(this);
		out.close();
	}

	public static Linear loadFrom(String file) throws LoadModelException{
		Linear cl = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(
					new BufferedInputStream(new FileInputStream(file))));
			cl = (Linear) in.readObject();
			in.close();
		} catch (Exception e) {
			throw new LoadModelException(e,file);
		}
		return cl;
	}

	public Inferencer getInferencer() {
		return inferencer;
	}
	
	public void setInferencer(Inferencer inferencer)	{
		this.inferencer = inferencer;
	}

	public AlphabetFactory getAlphabetFactory() {
		return factory;
	}

	public void setWeights(float[] weights) {
		inferencer.setWeights(weights);
	}

	public float[] getWeights() {
		return inferencer.getWeights();
	}

	public void setPipe(Pipe pipe) {
		this.pipe = pipe;		
	}
	public Pipe getPipe() {
		return pipe;		
	}

}
