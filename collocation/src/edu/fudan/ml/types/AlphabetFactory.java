package edu.fudan.ml.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * 特征和标签索引字典管理器
 * @author Feng Ji
 */
public final class AlphabetFactory implements Serializable {

	private static final long serialVersionUID = 4949560459448660488L;

	public static final String DefalutFeatureName = "feature";
	public static final String DefalutLabelName = "label";
	private Map<String, AbstractAlphabet> maps = null;

	private AlphabetFactory() {
		maps = new HashMap<String, AbstractAlphabet>();
	}

	private AlphabetFactory(Map<String, AbstractAlphabet> maps) {
		this.maps = maps;
	}

	/**
	 * 构造词典管理器
	 * @return 词典工厂
	 */
	public static AlphabetFactory buildFactory() {
			return new AlphabetFactory();
	}

	/**
	 * 构造特征词典
	 * @param name 词典名称
	 * @return 特征词典
	 */
	public FeatureAlphabet buildFeatureAlphabet(String name)	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey(name))	{
			maps.put(name, new FeatureAlphabet());
			alphabet = maps.get(name);
		}else	{
			alphabet = maps.get(name);
			if (!(alphabet instanceof FeatureAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (FeatureAlphabet) alphabet;
	}
	/**
	 * 建立缺省的特征字典
	 * @return 缺省特征词典
	 */
	public FeatureAlphabet DefaultFeatureAlphabet()	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey(DefalutFeatureName))	{
			maps.put("feature", new FeatureAlphabet());
			alphabet = maps.get(DefalutFeatureName);
		}else	{
			alphabet = maps.get(DefalutFeatureName);
			if (!(alphabet instanceof FeatureAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (FeatureAlphabet) alphabet;
	}

	/**
	 * 重建特征词典
	 * @param name 词典名称
	 * @return 特征词典
	 */
	public FeatureAlphabet rebuildFeatureAlphabet(String name)	{
		FeatureAlphabet alphabet = null;
		if (maps.containsKey(name))	{
			alphabet = (FeatureAlphabet) maps.get(name);
			alphabet.clear();
		}else{
			alphabet = new FeatureAlphabet();
			maps.put(name, alphabet);
		}
		return alphabet;
	}

	/**
	 * 构造类别词典
	 * @param name 词典名称
	 * @return 类别词典
	 */
	public LabelAlphabet buildLabelAlphabet(String name)	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey(name))	{
			maps.put(name, new LabelAlphabet());
			alphabet = maps.get(name);
		}else	{
			alphabet = maps.get(name);
			if (!(alphabet instanceof LabelAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (LabelAlphabet) alphabet;
	}
	/**
	 * 建立缺省的标签字典
	 * @return 标签字典
	 */
	public LabelAlphabet DefaultLabelAlphabet()	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey(DefalutLabelName))	{
			maps.put("label", new LabelAlphabet());
			alphabet = maps.get(DefalutLabelName);
		}else	{
			alphabet = maps.get(DefalutLabelName);
			if (!(alphabet instanceof LabelAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (LabelAlphabet) alphabet;
	}

	/**
	 * 得到类别数量 y
	 * @return 别数量 
	 */
	public int getLabelSize() {
		return DefaultLabelAlphabet().size();
	}
	/**
	 * 得到特征数量 f(x,y)
	 * @return 特征数量 
	 */
	public int getFeatureSize() {
		return DefaultFeatureAlphabet().size();
	}

	/**
	 *  不再增加新的词
	 * @param b
	 */
	public void setStopIncrement(boolean b) {
		Iterator<String> it = maps.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			maps.get(key).setStopIncrement(b);
		}
	}
}
