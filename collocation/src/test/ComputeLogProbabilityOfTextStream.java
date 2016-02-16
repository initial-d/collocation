package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.collections.Iterators;
import edu.berkeley.nlp.lm.io.IOUtils;
import edu.berkeley.nlp.lm.io.LmReaders;
import edu.berkeley.nlp.lm.util.Logger;

/**
 * Computes the log probability of a list of files. With the <code>-g</code>
 * option, it interprets the next two arguments as a <code>vocab_cs.gz</code>
 * file (see {@link LmReaders} for more detail) and a Berkeley LM binary,
 * respectively. Without <code>-g</code>, it interprets the next file as a
 * Berkeley LM binary. All remaining files are treated as plain-text (possibly
 * gzipped) files which have one sentence per line; a dash is used to indicate
 * that text should from standard input. If no files are given, reads from
 * standard input.
 */
public class ComputeLogProbabilityOfTextStream {
	/**
	 * @param files
	 * @param lm
	 * @throws IOException
	 */
	private NgramLanguageModel<String> lm;
	private List<Double> ans;
	
	public ComputeLogProbabilityOfTextStream() {
		String vocabFile = null;
		String binaryFile = "E:\\STUDY\\JAVA\\collocation\\lm\\kneserNeyFromText.binary";
		lm = readBinary(vocabFile, binaryFile);
	}
	
	public  int computeProb(List<String> candsents) throws IOException {
		ans = new ArrayList<Double>();
		for (String line : candsents) {
			List<String> words = Arrays.asList(line.trim().split("\\s+"));
			double sentenceProb = lm.scoreSentence(words);
			ans.add(sentenceProb);
		}
		if(ans.isEmpty()) {
			return 0;
		}
		double INF = ans.get(0).doubleValue();
		int pos = 0;
		for (int i = 1; i < ans.size(); i++) {
			if (ans.get(i).doubleValue() > INF) {
				INF = ans.get(i).doubleValue();
				pos = i;
			}
		}
		
		return pos;
	}
	
	public double computemrr(List<String> cand, String real) {
		if(ans.isEmpty()) {
			return 0.0;
		}
		
		int pos = -1;
		double temp = 0.0;
		for (int i = 0; i < cand.size(); i++) {
			if(real.equals(cand.get(i))) {
				pos = i;
				break;
			}
		}
		
		if(pos == -1) {
			return 0.0;
		}
		
		temp = ans.get(pos);
		int no = 0;
		for(int i = 0; i < ans.size(); i++) {
			if(ans.get(i) >= temp) {
				no++;
			}
		}
		
		return (double)(1.0) / (double)(no);
	}

	/**
	 * @param vocabFile
	 * @param binaryFile
	 * @return
	 */
	private static NgramLanguageModel<String> readBinary(String vocabFile,
			String binaryFile) {
		NgramLanguageModel<String> lm;
		if (vocabFile != null) {
			Logger.startTrack("Reading Google Binary " + binaryFile
					+ " with vocab " + vocabFile);
			lm = LmReaders.readGoogleLmBinary(binaryFile, vocabFile);
			Logger.endTrack();
		} else {
			Logger.startTrack("Reading LM Binary " + binaryFile);
			lm = LmReaders.readLmBinary(binaryFile);
			Logger.endTrack();
		}
		return lm;
	}

}
