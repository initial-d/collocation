package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import edu.fudan.ml.loss.Loss;
import edu.fudan.util.exception.LoadModelException;
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
import features.my_feature;
import test.my_test;

public class UserSentence {

	public String str;
	public String Englisharticle="";
	public List<String> own_features = new ArrayList<String>();
	public List<String> verbAndnone = new ArrayList<String>();
	public List<String> results = new ArrayList<String>();
	public my_feature ft = new my_feature();
	public static ComputeLogProbabilityOfTextStream cal = new ComputeLogProbabilityOfTextStream();
	public static LexicalizedParser lp;
	public static Properties props;
	public static StanfordCoreNLP pipeline;

	//สตภýปฏ
	public UserSentence(String str) throws FileNotFoundException{
		this.str = str;
	}
	public UserSentence()
	{
		lp = LexicalizedParser
				.loadModel("E:\\STUDY\\JAVA\\collocation\\src\\englishPCFG.ser.gz");
		props = new Properties();
		props.put("annotators", "tokenize,ssplit,pos, lemma");
		pipeline = new StanfordCoreNLP(props);
	}
	public void SetStr(String str)
	{
		this.str = str;
	}

	public void test() throws Exception {
		verbAndnone = ft.extractVNs(str);
		own_features = ft.extractor(str, verbAndnone);
		my_test.test(this);

	}
	
	public int getNum(){
		return verbAndnone.size();
	}
	
	public void Getresult()
	{
		System.out.println("****************************************");
		for(String str: results)
		{
			System.out.println(str);
		}
		System.out.println("****************************************");
	}
	
	public List<String> Getsents(String path) throws IOException
	{
		InputStreamReader in = null;
		List<String> strSents = new ArrayList<String>();
		in = new InputStreamReader(new FileInputStream(path));
		BufferedReader bufferedReader = new BufferedReader(in);
		String lineTxt = null;
		System.out.println('\n');
		while ((lineTxt = bufferedReader.readLine()) != null) {
			System.out.println(lineTxt);
			Englisharticle+=lineTxt;
		}
		int index = 0;
		for(int i=0; i<Englisharticle.length(); i++)
		{
			char ch = Englisharticle.charAt(i);
			if(ch == '.' || ch == '!' || ch=='?')
			{
				String temp = Englisharticle.substring(index, i);
				strSents.add(temp);
				index = i+1;
			}
		}
		return strSents;
	}
	
}
