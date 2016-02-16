package edu.fudan.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 实现数组排序、直方图的功能
 * 
 * @author xpqiu
 * @version 1.0
 * @since FudanNLP 1.5
 */
public class MyArrays {
	/**
	 * 记录之前的label和得分，保留前n个
	 * 
	 * @param score
	 * @param pred
	 * @return 插入位置
	 */
	public static int addBest(float[] scores, Object[] predList, float score, Object pred) {
		int n = scores.length;
		int i;
		for (i = 0; i < n; i++) {
			if (score > scores[i])
				break;
		}
		if (i >= n)
			return -1;
		for (int k = n - 2; k >= i; k--) {
			scores[k + 1] = scores[k];
			predList[k + 1] = predList[k];
		}
		scores[i] = score;
		predList[i] = pred;
		return i;
	}

	/**
	 * 
	 * @param count
	 * @param nbin
	 * @return 直方图
	 */
	public static float[][] histogram(float[] count, int nbin) {
		float maxCount = Float.NEGATIVE_INFINITY;
		float minCount = Float.MAX_VALUE;
		for (int i = 0; i < count.length; i++) {
			if (maxCount < count[i]) {
				maxCount = count[i];
			}
			if (minCount > count[i]) {
				minCount = count[i];
			}
		}
		float[][] hist = new float[2][nbin];
		float interv = (maxCount - minCount) / nbin;
		for (int i = 0; i < count.length; i++) {
			int idx = (int) Math.floor((count[i] - minCount) / interv);
			if (idx == nbin)
				idx--;
			hist[0][idx]++;
		}
		for (int i = 0; i < nbin; i++) {
			hist[1][i] = minCount + i * interv;
		}
		return hist;
	}

	/**
	 * 归一化
	 * 
	 * @param c
	 */
	public static void normalize(float[] c) {
		float max = Float.MIN_VALUE;
		float min = Float.MAX_VALUE;
		for (int i = 0; i < c.length; i++) {
			if (min > c[i])
				min = c[i];
			if (max < c[i])
				max = c[i];
		}
		float val = max - min;
		if (val == 0)
			return;
		for (int i = 0; i < c.length; i++) {
			c[i] = (c[i] - min) / val;
		}
	}

	/**
	 * 对数组的绝对值由大到小排序，返回调整后元素对于的原始下标
	 * 
	 * @param c
	 *            待排序数组
	 * @return 原始下标
	 */
	public static int[] sort(float[] c) {

		HashMap<Integer, Float> map = new HashMap<Integer, Float>();

		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0.0) {
				map.put(i, Math.abs(c[i]));
			}
		}
		ArrayList<Map.Entry<Integer, Float>> list = new ArrayList<Map.Entry<Integer, Float>>(
				map.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
			@Override
			public int compare(Entry<Integer, Float> o1,
					Entry<Integer, Float> o2) {

				if (o2.getValue() > o1.getValue()) {
					return 1;
				} else if (o1.getValue() > o2.getValue()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		int[] idx = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			idx[i] = list.get(i).getKey();
		}
		return idx;
	}

	/**
	 * 得到总能量值大于thres的元素对应的下标
	 * 
	 * @param c
	 * @param thres
	 * @param r
	 *            true表示返回最大的，false表示返回剩余的
	 * @return 元素下标
	 */
	public static int[] getTop(float[] c, float thres, boolean r) {
		int[] idx = sort(c);
		int i;
		float total = 0;
		float[] cp = new float[idx.length];
		for (i = 0; i < idx.length; i++) {
			cp[i] = (float) Math.pow(c[idx[i]], 2);
			total += cp[i];
		}

		float ratio = 0;
		for (i = 0; i < idx.length; i++) {
			ratio += cp[i] / total;
			if (ratio > thres)
				break;
		}
		int[] a;
		if (r)
			a = Arrays.copyOfRange(idx, 0, i);
		else
			a = Arrays.copyOfRange(idx, i, idx.length);
		return a;
	}

	/**
	 * 对部分下标的元素赋值
	 * 
	 * @param c
	 *            数组
	 * @param idx
	 *            赋值下标
	 * @param v
	 *            值
	 */
	public static void set(float[] c, int[] idx, float v) {
		for (int i = 0; i < idx.length; i++) {
			c[idx[i]] = v;
		}
	}
	
	/**
	 * 移除能量值小于一定阈值的项
	 * @param c 数组
	 * @param v 阈值
	 */
	public static void trim(float[] c, float v) {
		int[] idx = getTop(c, v, false);
		set(c, idx, 0.0f);
	}

	/**
	 * 求和 
	 * @param c
	 * @return 所有元素的和
	 */
	public static int sum(int[] c) {
		int s = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0)
				s+=c[i];
		}
		return s;
	}
	/**
	 * 累加
	 * @param c
	 * @return 所有元素的和
	 */
	public static int[] accumulate(int[] c) {
		int[] s = new int[c.length];
		s[0] =c[0];
		for (int i = 1; i < c.length; i++) {			
				s[i]+=s[i-1]+c[i];
		}
		return s;
	}
	/**
	 * 求和 
	 * @param c float
	 * @return 所有元素的和
	 */
	public static float sum(float[] c) {
		float s = 0;
		for (int i = 0; i < c.length; i++) {
			s+=c[i];
		}
		return s;
	}
	
	/**
	 * 统计非零个数
	 * 
	 * @param c
	 * @return 非零元素数量
	 */
	public static int countNoneZero(float[] c) {
		int count = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0.0)
				count++;
		}
		return count;
	}

	/**
	 * 统计非零元素
	 * 
	 * @param c
	 * @return 非零元素标记
	 */
	public static boolean[] getNoneZeroIdx(float[] c) {
		boolean[] b = new boolean[c.length];
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0.0)
				b[i] = true;
		}
		return b;
	}

	public static int[] string2int(String[] c) {
		int[] d = new int[c.length];
		for (int i = 0; i < c.length; i++) {
			d[i] = Integer.parseInt(c[i]);
		}
		return d;
	}
	public static String[] int2string(int[] c) {
		String[] d = new String[c.length];
		for (int i = 0; i < c.length; i++) {
			d[i] = String.valueOf(c[i]);
		}
		return d;
	}
}
