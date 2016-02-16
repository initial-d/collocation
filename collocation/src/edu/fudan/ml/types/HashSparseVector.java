package edu.fudan.ml.types;

import gnu.trove.iterator.TIntFloatIterator;
import gnu.trove.map.hash.TIntFloatHashMap;

import java.io.Serializable;
import java.util.ArrayList;

public class HashSparseVector implements Serializable {

	private static final long serialVersionUID = 2797070318099414849L;

	public TIntFloatHashMap data = new TIntFloatHashMap();

	public HashSparseVector(float[] w){
		for (int i = 0; i < w.length; i++) {
			if (Math.abs((w[i]-0f))>Float.MIN_VALUE) {
				put(i, w[i]);
			}
		}
	}

	public HashSparseVector(float[] w, boolean b) {
		for (int i = 0; i < w.length; i++) {
			if (Math.abs((w[i]-0f))>Float.MIN_VALUE) {
				put(i, w[i]);
			}
		}
		if(b)
			put(w.length,1.0f);
	}


	public HashSparseVector() {

	}

	public HashSparseVector(HashSparseVector v) {
		data = new TIntFloatHashMap(v.data);
	}

	public void minus(HashSparseVector sv) {


	}


	public void add(int id, float c) {
		data.adjustOrPutValue(id, c, c);
	}


	/**
	 * v + sv
	 * @param sv
	 */
	public void plus(HashSparseVector sv) {
		TIntFloatIterator it = sv.data.iterator();
		while(it.hasNext()){
			it.advance();
			data.adjustOrPutValue(it.key(), it.value(),it.value());
		}

	}

	/**
	 * v + sv*w
	 * @param sv
	 * @param w
	 */
	public void plus(HashSparseVector sv, float w) {
		TIntFloatIterator it = sv.data.iterator();
		while(it.hasNext()){
			it.advance();
			float v = it.value()*w;
			data.adjustOrPutValue(it.key(), v,v);
		}
	}



	public int[] indices() {
		return data.keys();
	}



	/**
	 * 点积 v.*sv
	 * @param sv
	 * @return
	 */
	public float dotProduct(HashSparseVector sv) {
		float v =0f;
		if(sv.size() < data.size()){
			TIntFloatIterator it = sv.data.iterator();			
			while(it.hasNext()){
				it.advance();
				v += data.get(it.key())*it.value();
			}
		}else{
			TIntFloatIterator it = data.iterator();			
			while(it.hasNext()){
				it.advance();
				v += sv.data.get(it.key())*it.value();
			}
		}
		return v;
	}

	/**
	 * 点积
	 * @param vector
	 * @return
	 */
	public float dotProduct(float[] vector) {
		float v =0f;
		TIntFloatIterator it = data.iterator();			
		while(it.hasNext()){
			it.advance();
			if(it.key() >= vector.length) {
				continue;
			}
			v += vector[it.key()] * it.value();
		}
		return v;
	}


	public float l2Norm2() {
		TIntFloatIterator it = data.iterator();
		float norm = 0f;
		while(it.hasNext()){
			it.advance();
			norm += it.value()*it.value();
		}
		return norm;
	}




	public float get(int id) {
		return data.get(id);
	}


	public void put(int id, float value) {
		data.put(id, value);
	}
	public void put(int[] idx, float c) {
		for(int i=0;i<idx.length;i++){
			if(idx[i]!=-1)
			data.put(idx[i], c);
		}
	}


	public float remove(int id) {
		return data.remove(id);
	}


	public int size() {
		return data.size();
	}


	public boolean containsKey(int id) {
		return data.containsKey(id);
	}

	public void clear() {
		data.clear();		
	}

	/**
	 * 
	 * @param c
	 */
	public void scaleDivide(float c) {
		TIntFloatIterator it = data.iterator();
		while(it.hasNext()){
			it.advance();
			float v = it.value()/c;
			data.put(it.key(), v);
		}

	}
	/**
	 * 欧氏距离
	 * @param sv1
	 * @param sv2
	 * @return
	 */
	public static float distanceEuclidean(HashSparseVector sv1 ,HashSparseVector sv2) {
		float dist = 0.0f;
		TIntFloatIterator it1 = sv1.data.iterator();
		TIntFloatIterator it2 = sv2.data.iterator();
		while(it1.hasNext()&&it2.hasNext()){
			it1.advance();
			it2.advance();
			if(it1.key()<it2.key()){
				dist += it1.value()*it1.value();
				it1.advance();
			}else if(it1.key()>it2.key()){
				dist += it2.value()*it2.value();
				it2.advance();
			}else{
				float t = it1.value() - it2.value();
				dist += t*t;
				it1.advance();
				it2.advance();
			}
		}
		return dist;
	}

	/**
	 * 欧氏距离
	 * @param sv
	 * @return
	 */
	public float distanceEuclidean(HashSparseVector sv) {
		return distanceEuclidean(this,sv);
	}


	public String toString(){
		StringBuilder sb = new StringBuilder();
		TIntFloatIterator it = data.iterator();
		while(it.hasNext()){
			it.advance();
			sb.append(it.key());
			sb.append(":");
			sb.append(it.value());
			if(it.hasNext())
				sb.append(", ");
		}
		return sb.toString();
	}






}
