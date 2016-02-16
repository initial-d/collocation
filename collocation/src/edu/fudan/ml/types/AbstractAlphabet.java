package edu.fudan.ml.types;


import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.Serializable;

/**
 * 抽象词典类
 * @author Feng Ji
 *
 */
public abstract class AbstractAlphabet implements Serializable {

	private static final long serialVersionUID = -6803250687142456011L;
	
	/**
	 * 数据
	 */
	protected TObjectIntHashMap<String> data;
	/** the default capacity for new collections */
    public static final int DEFAULT_CAPACITY = 10;

    /** the load above which rehashing occurs. */
    public static final float DEFAULT_LOAD_FACTOR = 0.5f;
    
    int noEntryValue = -1;
    
	protected boolean frozen;
	
	AbstractAlphabet()	{
		data = new TObjectIntHashMap<String>(DEFAULT_CAPACITY,DEFAULT_LOAD_FACTOR,noEntryValue);
		frozen = false;
	}
	
	/**
	 * 判断词典是否冻结
	 * @return true - 词典冻结；false - 词典未冻结
	 */
	public boolean isStopIncrement() {
		return frozen;
	}

	/**
	 * 不再增加新的词
	 * @param stopIncrement
	 */
	public void setStopIncrement(boolean stopIncrement) {
		this.frozen = stopIncrement;
	}
	
	/**
	 * 查找字符串索引编号
	 * @param str 字符串
	 * @return 索引编号
	 */
	public abstract int lookupIndex(String str);
	
	/**
	 * 词典大小
	 * @return 词典大小
	 */
	public abstract int size();

	/**
	 * 恢复成新字典
	 */
	public void clear() {
		data.clear();		
	}

}