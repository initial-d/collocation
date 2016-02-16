package utils;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class lemma {
	public static String lemm(String str) {
		String lema = null;
		Properties props = new Properties();
		//props.put("annotators", "tokenize,ssplit,pos, lemma");
		props.put("annotators", "tokenize,ssplit,pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(str);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		//System.out.println(sentences);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				//String word = token.get(TextAnnotation.class);
				lema = token.get(LemmaAnnotation.class);
			}
		}
		return lema;
	}
	/*
	public static void main(String[] args) {
		System.out.println(lemm("apples"));
	}
	*/
}
