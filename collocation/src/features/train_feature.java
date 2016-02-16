package features;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
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
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

/*
 * 
 * 从句子中抽取特征
 * 
 * author duyimin
 */
public class train_feature {
	private static LexicalizedParser lp;
	private static Properties props;
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
	public static String extract_vn(String str) {
		String temp = (String) str.subSequence(5, str.length() - 1);
		String vs = temp.substring(0, temp.indexOf(" ") - 1);
		String ns = temp.substring(temp.indexOf(" ") + 1, temp.length());
		int index = -1;
		for(int i = 0; i < vs.length(); i++) {
			if(!Character.isLetter(vs.charAt(i))) {
				index = i;
				break;
			}
		}
		if(index == -1) {
			return null;
		}
		String v = vs.substring(0, index);
		index = -1;
		for(int i = 0; i < ns.length(); i++) {
			if(!Character.isLetter(ns.charAt(i))) {
				index = i;
				break;
			}
		}
		if(index == -1) {
			return null;
		}
		String n = ns.substring(0, index);
		String lemm_v = v;
		String lemm_n = n;
		if(lemm_v == null || lemm_n == null) {
			return null;
		}
		return lemm_v + "_" + lemm_n;
	}
	
	public static List<String> dobj(String str) {
	    String sent2 = str;
	    TokenizerFactory tokenizerFactory =
	    PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    List rawWords2 =
	    		tokenizerFactory.getTokenizer(new StringReader(sent2)).tokenize();
	    Tree parse = lp.apply(rawWords2);
	    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	    List<String> res = new ArrayList<String>();
	    for(TypedDependency tdl1:tdl){
		     if(tdl1.reln().toString() == "dobj") {
		    	 res.add(tdl1.toString());
		     }
		}
		return res;
	}
	
	private static  String[] Tokenize(String str) throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("F:\\workspace\\collocation\\src\\en-token.bin");
	 
		TokenizerModel model = new TokenizerModel(is);
	 
		Tokenizer tokenizer = new TokenizerME(model);
	 
		String tokens[] = tokenizer.tokenize(str);	 
		is.close();
		return tokens;
	}
	
	private static boolean notpron(String str) {
		if(str.equals(",") || str.equals(".") || str.equals("?") || str.equals("!")
				|| str.equals(":") || str.equals(";")) {
			return false;
		}
		return true;
	}
	
	private static String extract(String filepath, String vn) {
		InputStreamReader in = null;		
		try {
			in = new InputStreamReader(new FileInputStream(filepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(in);
        String lineTxt = null;
        int no = 0;
        FileOutputStream log = null;
		try {
			log = new FileOutputStream(new File("F:\\workspace\\collocation\\data\\" + vn));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        try {
        	String UnivL;
        	String UnivR;
        	String UninL;
        	String UninR;
        	String UnivLL;
        	String UnivRR;
        	String UninLL;
        	String UninRR;
        	String BivL;
        	String BivR;
        	String BinL;
        	String BinR;
        	String BivI;
        	String BinI;
			while((lineTxt = bufferedReader.readLine()) != null){
				if(lineTxt != null){
					
					no++;
					UnivL = null;
					UnivR = null;
					UninL = null;
					UninR = null;
					UnivLL = null;
					UnivRR = null;
					UninLL = null;
					UninRR = null;
					BivL = null;
					BivR = null;
					BinL = null;
					BinR = null;
					BivI = null;
					BinI = null;
					String [] tokens = Tokenize(lineTxt);
					List<String> dobjs = dobj(lineTxt);
					for(int i = 0; i < dobjs.size(); i++) {
						String k = extract_vn(dobjs.get(i));
						if(k != null) {
							String v = k.substring(0, k.indexOf('_'));
							String n = k.substring(k.indexOf('_') + 1);
							String temp = lemm(v) + '_' + lemm(n);
							if(!temp.equals(vn)) {
								
								continue;
							}
							int index_v = -1;
							int index_n = -1;	
							System.out.println(v + " " + n);
							for(int j = 0; j < tokens.length; j++) {
								
								if(tokens[j].equals(v)) {
									index_v = j;
									if(index_n != -1) {
										break;
									}
								}
								if(tokens[j].equals(n)) {
									index_n = j;
									if(index_v != -1) {
										break;
									}
								}
							}
							System.out.println(index_v + " " + index_n );
							if(index_v == -1 || index_n == -1) {
								break;
							}
							String ans = "0";
							System.out.println(ans);
							
							if(index_v - 1 >= 0 && notpron(tokens[index_v - 1])) {
								UnivL = "UnivL_" + lemm(tokens[index_v - 1]);
								ans += ' ' + UnivL;
							}
							if(index_v - 2 >= 0 && notpron(tokens[index_v - 2]) && notpron(tokens[index_v - 1])) {
								UnivLL = "UnivLL_" + lemm(tokens[index_v - 2]);
								BivL = "BivL_" + lemm(tokens[index_v - 1]) + '_' + lemm(tokens[index_v - 2]);
								ans += ' ' + BivL;
							}
							if(index_v + 1 < tokens.length && notpron(tokens[index_v + 1])) {
								UnivR = "UnivR_" + lemm(tokens[index_v + 1]);
								ans += ' ' + UnivR;
							}
							if(index_v + 2 < tokens.length && notpron(tokens[index_v + 2]) && notpron(tokens[index_v + 1])) {
								UnivRR = "UnivRR_" + lemm(tokens[index_v + 2]);
								BivR = "BivR_" + lemm(tokens[index_v + 1]) + '_' + lemm(tokens[index_v + 2]);
								ans += ' ' + BivR;
							}
							if(index_n - 1 >= 0 && notpron(tokens[index_n - 1])) {
								UninL = "UninL_" + lemm(tokens[index_n - 1]);
								ans += ' ' + UninL;
							}
							if(index_n - 2 >= 0 && notpron(tokens[index_n - 2]) && notpron(tokens[index_n - 1])) {
								UninLL = "UninLL_" + lemm(tokens[index_n - 2]);
								BinL = "BinL_" + lemm(tokens[index_n - 1]) + '_' + lemm(tokens[index_n - 2]);
								ans += ' ' + BinL;
							}
							if(index_n + 1 < tokens.length && notpron(tokens[index_n + 1])) {
								UninR = "UninR_" + lemm(tokens[index_n + 1]);
								ans += ' ' + UninR;
							}
							if(index_n + 2 < tokens.length && notpron(tokens[index_n + 2]) && notpron(tokens[index_n + 2])) {
								UninRR = "UninRR_" + lemm(tokens[index_n + 2]);
								BinR = "BinR_" + lemm(tokens[index_n + 1]) + '_' + lemm(tokens[index_n + 2]);
								ans += ' ' + BinR;
							}
							if(index_v - 1 >= 0 && notpron(tokens[index_v - 1]) &&
									index_v + 1 < tokens.length && notpron(tokens[index_v + 1])) {
								BivI = "BivI_" + lemm(tokens[index_v - 1]) + '_' + lemm(tokens[index_v + 1]);
								ans += ' ' + BivI;
							}
							if(index_n - 1 >= 0 && notpron(tokens[index_n - 1]) &&
									index_n + 1 < tokens.length && notpron(tokens[index_n + 1])) {
								BinI = "BinI_"  + lemm(tokens[index_n - 1]) + '_' + lemm(tokens[index_n + 1]);
								ans += ' ' + BinI;
							}
							
							System.out.println(ans);
							log.write((ans +"\r\n").getBytes());
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			log.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static List<String> getFiles(String filePath) {
		List<String> ans = new ArrayList<String>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {				
			ans.add(file.getAbsolutePath());
		}
		return ans;
	}
	public static void pre_data() {
		lp = LexicalizedParser.loadModel("F:\\workspace\\collocation\\src\\englishPCFG.ser.gz");
		props = new Properties();
		props.put("annotators", "tokenize,ssplit,pos, lemma");
		pipeline = new StanfordCoreNLP(props);
		List<String> files = getFiles("F:\\eclipse\\traindata\\");
		for(int i = 0; i < files.size(); i++) {
			String txt = files.get(i).substring(files.get(i).lastIndexOf('\\') + 1);
			//System.out.println(txt.substring(0, txt.indexOf('.')));
			extract(files.get(i), txt.substring(0, txt.indexOf('.')));
		}
	}
	public static void main(String [] args) {
		pre_data();
	}
}
