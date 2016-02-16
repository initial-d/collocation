package training;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class lm_lemma {
	private static StanfordCoreNLP pipeline;

	public static String lemm(String str) {
		String lema = null;
		Annotation document = new Annotation(str);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				lema = token.get(LemmaAnnotation.class);
			}
		}
		return lema;
	}

	private static String[] Tokenize(String str) throws InvalidFormatException,
			IOException {
		InputStream is = new FileInputStream(
				"F:\\workspace\\collocation\\src\\en-token.bin");

		TokenizerModel model = new TokenizerModel(is);

		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(str);
		is.close();
		return tokens;
	}

	public static void main(String[] args) {
		Properties props;
		props = new Properties();
		props.put("annotators", "tokenize,ssplit,pos, lemma");
		pipeline = new StanfordCoreNLP(props);
		
		FileOutputStream log = null;
		try {
			log = new FileOutputStream(new File(
					"F:\\workspace\\collocation\\lm\\traindata"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(new FileInputStream(
					"F:\\workspace\\collocation\\lm\\traindata.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedReader bufferedReader = new BufferedReader(in);
		String lineTxt = null;
		try {
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String[] tokens = Tokenize(lineTxt);
				if(tokens.length < 3) {
					continue;
				}
				String ans = lemm(tokens[0]);
				for (int j = 1; j < tokens.length; j++) {
					ans += ' ' + lemm(tokens[j]);
				}
				
				log.write((ans + "\r\n").getBytes());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
