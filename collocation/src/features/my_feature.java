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
import test.UserSentence;
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
public class my_feature {
	private static LexicalizedParser lp;
	private static Properties props;
	private static StanfordCoreNLP pipeline;

	public String lemm(String str) {
		String lema = null;
		Annotation document = new Annotation(str);
		UserSentence.pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				lema = token.get(LemmaAnnotation.class);
			}
		}
		return lema;
	}

	public my_feature()
	{
		/*lp = LexicalizedParser
				.loadModel("E:\\STUDY\\JAVA\\collocation\\src\\englishPCFG.ser.gz");
		props = new Properties();
		props.put("annotators", "tokenize,ssplit,pos, lemma");
		pipeline = new StanfordCoreNLP(props);*/
	}
	public static String extract_vn(String str) {
		String temp = (String) str.subSequence(5, str.length() - 1);
		String vs = temp.substring(0, temp.indexOf(" ") - 1);
		String ns = temp.substring(temp.indexOf(" ") + 1, temp.length());
		int index = -1;
		for (int i = 0; i < vs.length(); i++) {
			if (!Character.isLetter(vs.charAt(i))) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return null;
		}
		String v = vs.substring(0, index);
		index = -1;
		for (int i = 0; i < ns.length(); i++) {
			if (!Character.isLetter(ns.charAt(i))) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return null;
		}
		String n = ns.substring(0, index);
		String lemm_v = v;
		String lemm_n = n;
		if (lemm_v == null || lemm_n == null) {
			return null;
		}
		return lemm_v + "_" + lemm_n;
	}

	public List<String> dobj(String str) {
		String sent2 = str;
		TokenizerFactory tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		List rawWords2 = tokenizerFactory.getTokenizer(new StringReader(sent2))
				.tokenize();
		Tree parse = UserSentence.lp.apply(rawWords2);
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		List<String> res = new ArrayList<String>();
		for (TypedDependency tdl1 : tdl) {
			if (tdl1.reln().toString() == "dobj") {
				res.add(tdl1.toString());
			}
		}
		return res;
	}

	private  String[] Tokenize(String str) throws InvalidFormatException,
			IOException {
		InputStream is = new FileInputStream(
				"E:\\STUDY\\JAVA\\collocation\\src\\en-token.bin");

		TokenizerModel model = new TokenizerModel(is);

		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(str);
		is.close();
		return tokens;
	}

	private  boolean notpron(String str) {
		if(str.equals(",") || str.equals(".") || str.equals("?") || str.equals("!")
				|| str.equals(":") || str.equals(";")) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("resource")
	private  List<String> extract(String test_0, String test_1, String path) throws InvalidFormatException {
		List<String> real = new ArrayList<String>();
		List<String> now = new ArrayList<String>();
		List<String> sentenceFeature = new ArrayList<String>();
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(new FileInputStream(test_0));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(in);
		String lineTxt = null;
		try {
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String r = lineTxt.substring(0, lineTxt.indexOf(' '));
				String n = lineTxt.substring(lineTxt.indexOf(' ') + 1);
				real.add(r);
				now.add(n);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in = new InputStreamReader(new FileInputStream(test_1));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bufferedReader = new BufferedReader(in);
		lineTxt = null;
		FileOutputStream log = null;
		FileOutputStream log0 = null;
		FileOutputStream log1 = null;
		try {
			//test2文件保存的是特征
			log = new FileOutputStream(new File(
					"E:\\STUDY\\JAVA\\collocation\\test\\test_2"));
			//test0文件保存的是句子中的动名词搭配
			log0 = new FileOutputStream(new File(
					"E:\\STUDY\\JAVA\\collocation\\test\\test_0"));
			//test1文件保存的是句子
			log1 = new FileOutputStream(new File(
					"E:\\STUDY\\JAVA\\collocation\\test\\test_1"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		int no = 0;
		try {
			while ((lineTxt = bufferedReader.readLine()) != null) {
				if (lineTxt != null) {
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
					String[] tokens = Tokenize(lineTxt);
					List<String> dobjs = dobj(lineTxt);
					for (int i = 0; i < dobjs.size(); i++) {
						String k = extract_vn(dobjs.get(i));
						if (k != null) {
							String v = k.substring(0, k.indexOf('_'));
							String n = k.substring(k.indexOf('_') + 1);
							String temp = lemm(v) + '_' + lemm(n);
							//System.out.println(temp);
							if (!temp.equals(now.get(no))) {
								continue;
							}
							int index_v = -1;
							int index_n = -1;
							for (int j = 0; j < tokens.length; j++) {
								if (tokens[j].equals(v)) {
									index_v = j;
									if (index_n != -1) {
										break;
									}
								}
								if (tokens[j].equals(n)) {
									index_n = j;
									if (index_v != -1) {
										break;
									}
								}
							}
							if (index_v == -1 || index_n == -1) {
								break;
							}
							String ans = null;
							if(real.get(no).equals(now.get(no))) 
								ans = "0";
							else 
								ans = "1";
							
							
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
							
							//System.out.println(ans);
							log.write((ans + "\r\n").getBytes());
							sentenceFeature.add(ans+"\r\n");
							log0.write((real.get(no) + " " + now.get(no) + "\r\n").getBytes());
							log1.write((lineTxt + "\r\n").getBytes());
							break;
						}
					}
				}
				no++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			log.close();
			log0.close();
			log1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sentenceFeature;
	}

	//提取句子中所有的动名词搭配
	public  List<String> extractVNs(String lineTxt) throws InvalidFormatException, IOException {
		
		String[] tokens = Tokenize(lineTxt);
		List<String> dobjs = dobj(lineTxt);
		List<String> VNList = new ArrayList<String>();
		//提取句子动名词搭配
		for (int i = 0; i < dobjs.size(); i++) {
			String k = extract_vn(dobjs.get(i));
			if (k != null) {
				String v = k.substring(0, k.indexOf('_'));
				String n = k.substring(k.indexOf('_') + 1);
				String temp = lemm(v) + '_' + lemm(n);
				VNList.add(temp);
			}
		}
		return VNList;
	}
	
	

	public  List<String> extractor(String Englishsent,List<String> verbAndnone) throws InvalidFormatException {
		String lineTxt = Englishsent;
		String UnivL= null;
		String UnivR= null;
		String UninL= null;
		String UninR= null;
		String UnivLL= null;
		String UnivRR= null;
		String UninLL= null;
		String UninRR= null;
		String BivL= null;
		String BivR= null;
		String BinL= null;
		String BinR= null;
		String BivI= null;
    	String BinI= null;
    	
    	//提取后动词和名词的搭配
    	List<String> verbAndN = new ArrayList<String>();
    	//动名词搭配的特征
    	List<String> verbAndNFeatureList = new ArrayList<String>();

		try {
				String[] tokens = Tokenize(lineTxt);
				//提取句子特征
				for (String str : verbAndnone) {
					String ans = null;
					int index_v = -1;
					int index_n = -1;
					String[] tmpVN = str.split("_");
					String v = tmpVN[0];
					String n = tmpVN[1];
					for (int j = 0; j < tokens.length; j++) {
						if (lemm(tokens[j]).equals(v)) {
							index_v = j;
							if (index_n != -1) {
								break;
							}
						}
						if (lemm(tokens[j]).equals(n)) {
							index_n = j;
							if (index_v != -1) {
								break;
							}
						}
					}
					if (index_v == -1 || index_n == -1) {
						break;
					}

					if (index_v - 1 >= 0 && notpron(tokens[index_v - 1])) {
						UnivL = "UnivL_" + lemm(tokens[index_v - 1]);
						ans += ' ' + UnivL;
					}
					if (index_v - 2 >= 0 && notpron(tokens[index_v - 2])
							&& notpron(tokens[index_v - 1])) {
						UnivLL = "UnivLL_" + lemm(tokens[index_v - 2]);
						BivL = "BivL_" + lemm(tokens[index_v - 1]) + '_'
								+ lemm(tokens[index_v - 2]);
						ans += ' ' + BivL;
					}
					if (index_v + 1 < tokens.length
							&& notpron(tokens[index_v + 1])) {
						UnivR = "UnivR_" + lemm(tokens[index_v + 1]);
						ans += ' ' + UnivR;
					}
					if (index_v + 2 < tokens.length
							&& notpron(tokens[index_v + 2])
							&& notpron(tokens[index_v + 1])) {
						UnivRR = "UnivRR_" + lemm(tokens[index_v + 2]);
						BivR = "BivR_" + lemm(tokens[index_v + 1]) + '_'
								+ lemm(tokens[index_v + 2]);
						ans += ' ' + BivR;
					}
					if (index_n - 1 >= 0 && notpron(tokens[index_n - 1])) {
						UninL = "UninL_" + lemm(tokens[index_n - 1]);
						ans += ' ' + UninL;
					}
					if (index_n - 2 >= 0 && notpron(tokens[index_n - 2])
							&& notpron(tokens[index_n - 1])) {
						UninLL = "UninLL_" + lemm(tokens[index_n - 2]);
						BinL = "BinL_" + lemm(tokens[index_n - 1]) + '_'
								+ lemm(tokens[index_n - 2]);
						ans += ' ' + BinL;
					}
					if (index_n + 1 < tokens.length
							&& notpron(tokens[index_n + 1])) {
						UninR = "UninR_" + lemm(tokens[index_n + 1]);
						ans += ' ' + UninR;
					}
					if (index_n + 2 < tokens.length
							&& notpron(tokens[index_n + 2])
							&& notpron(tokens[index_n + 2])) {
						UninRR = "UninRR_" + lemm(tokens[index_n + 2]);
						BinR = "BinR_" + lemm(tokens[index_n + 1]) + '_'
								+ lemm(tokens[index_n + 2]);
						ans += ' ' + BinR;
					}
					if (index_v - 1 >= 0 && notpron(tokens[index_v - 1])
							&& index_v + 1 < tokens.length
							&& notpron(tokens[index_v + 1])) {
						BivI = "BivI_" + lemm(tokens[index_v - 1]) + '_'
								+ lemm(tokens[index_v + 1]);
						ans += ' ' + BivI;
					}
					if (index_n - 1 >= 0 && notpron(tokens[index_n - 1])
							&& index_n + 1 < tokens.length
							&& notpron(tokens[index_n + 1])) {
						BinI = "BinI_" + lemm(tokens[index_n - 1]) + '_'
								+ lemm(tokens[index_n + 1]);
						ans += ' ' + BinI;
					}

					verbAndNFeatureList.add(ans + "\r\n");
					
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		return verbAndNFeatureList;
	}	
	
	
	
	public List<String> pre_data(String sentence,List<String> verbAndnone) throws InvalidFormatException, IOException {

		return extractor(sentence,verbAndnone);
	}

	public static void main(String[] args) throws InvalidFormatException, IOException {
		//pre_data("");
		//System.out.printf("Task End");
	}
}
