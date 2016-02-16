package edu.fudan.ml.types;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * 特征词典
 * @author Feng Ji
 *
 */
public final class FeatureAlphabet extends AbstractAlphabet {

	private static final long serialVersionUID = -6187935479742068611L;

	/**
	 * 最后一个特征的位置
	 */
	private int last;

	public FeatureAlphabet() {
		super();
		last = 0;
	}

	@Override
	public int lookupIndex(String str) {
		return lookupIndex(str, 1);
	}

	/**
	 * 查询字符串索引编号
	 * @param str 字符串
	 * @param indent 间隔
	 * @return 字符串索引编号，-1表示词典中不存在字符串
	 */
	public int lookupIndex(String str, int indent) {
		if (indent < 1)
			throw new IllegalArgumentException(
					"Invalid Argument in FeatureAlphabet: " + indent);

		int ret = data.get(str);

		if (ret==-1 && !frozen) {//字典中没有，并且允许插入
			synchronized (this) {
				data.put(str, last);
				ret = last;
				last += indent;
			}
		}
		return ret;
	}

	@Override
	public int size() {
		return last;
	}
	/**
	 * 字典键的个数
	 * @return
	 */
	public int keysize() {
		return data.size();
	}
	
	/**
	 * 实际存储的数据大小
	 * @return
	 */
	public int nonZeroSize() {
		return this.data.size();
	}

	/**
	 * 索引对应的字符串是否存在在词典中
	 * @param id 索引
	 * @return 是否存在在词典中
	 */
	public boolean hasIndex(int id) {
		return data.containsValue(id);
	}

	public int remove(String str)	{
		int ret = -1;
		if (data.containsKey(str))	{
			ret = data.remove(str);
		}
		return ret;
	}

	public boolean adjust(String str, int adjust)	{
		return data.adjustValue(str, adjust);
	}

	public TObjectIntIterator<String> iterator()	{
		return data.iterator();
	}

	public void clear() {
		data.clear();
		last=0;
		frozen = false;
	}

	/**
	 * 按索引建立HashMap并返回
	 * @return 按“索引-特征字符串”建立的HashMap
	 */
	public TIntObjectHashMap<String> toInverseIndexMap() {
		TIntObjectHashMap<String> index = new TIntObjectHashMap<String>();
		TObjectIntIterator<String> it = data.iterator();
		while (it.hasNext()) {
			it.advance();
			String value = it.key();
			int key = it.value();
			index.put(key, value);
		}
		return index;
	}
	
}
