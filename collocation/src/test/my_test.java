package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import similarity.SimilarityCalculation;
import edu.fudan.data.reader.SimpleFileReader;
import edu.fudan.ml.classifier.Results;
import edu.fudan.ml.classifier.linear.Linear;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.nlp.pipe.StringArray2IndexArray;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.SeriesPipes;
import edu.fudan.nlp.pipe.Target2Label;
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
import features.test_feature;
import features.my_feature;
import test.UserSentence;
/**
 * 
 * @author yinhang
 * 
 */
public class my_test {
	static InstanceSet train;
	static InstanceSet test;
	static AlphabetFactory factory = AlphabetFactory.buildFactory();
	static LabelAlphabet al = factory.DefaultLabelAlphabet();
	static FeatureAlphabet af = factory.DefaultFeatureAlphabet();
	static String path = null;
	static List<String> vns;
	public static List<String> real;
	public static List<String> now;
	public static String test_sent;
	public static Properties props;
	public static StanfordCoreNLP pipeline;
	public static LexicalizedParser lp;
	public static Pipe pipe;
	
	public static String lemm(String str) {
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
	private static String[] Tokenize(String str) throws InvalidFormatException,
			IOException {
		InputStream is = new FileInputStream(
				"E:\\STUDY\\JAVA\\collocation\\src\\en-token.bin");

		TokenizerModel model = new TokenizerModel(is);

		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(str);
		is.close();
		return tokens;
	}

	public static List<String> dobj(String str) {
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
	//构造候选句子集
	public static List<String> build_candsents(List<String> candidate, int index) throws InvalidFormatException, IOException {
		List<String> candsents =  new ArrayList<String>();
		
		String[] tokens = Tokenize(test_sent);
		List<String> dobjs = dobj(test_sent);
		
		for (int i = 0; i < dobjs.size(); i++) {
			String k = test_feature.extract_vn(dobjs.get(i));
			if (k != null) {
				String v = k.substring(0, k.indexOf('_'));
				String n = k.substring(k.indexOf('_') + 1);
				String temp = lemm(v) + '_' + lemm(n);
				if (!temp.equals(now.get(index))) {
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
				

				for(int c = 0; c < candidate.size(); c++) {
					String[] cc = tokens;
					String str = candidate.get(c);
					String cv = str.substring(0, str.indexOf('_'));
					String cn = str.substring(str.indexOf('_') + 1);
					cc[index_v] = cv;
					cc[index_n] = cn;
					String ans = lemm(cc[0]);
					for(int cur = 1; cur < cc.length; cur++) {
						ans += ' ' + lemm(cc[cur]);
					}
					candsents.add(ans);
				}
			}
		}
		return candsents;
	}
	//输入参数 特征集,path
	public static void evaluate(List<String> devset, UserSentence usrsen,String path)
			throws LoadModelException, InvalidFormatException, IOException {
		
		SimilarityCalculation sc = new SimilarityCalculation();
		//System.out.println(test_sent);
		for (int i = 0; i < devset.size(); i++) {
			String one = devset.get(i);
			List<String> confuses = new ArrayList<String>();
			
			//call the simility，选取TOP15
			int k = 15;
			HashMap<String, Double> hash = new HashMap<String, Double>();
			String [] top_k =  new String[k];
			int count = 0;
			for(int j = 0; j < vns.size(); j++) {
				double sval = sc.run(now.get(i), vns.get(j));
				hash.put(vns.get(j), sval);
				
				if(count < k) 
					top_k[count++] = vns.get(j);
				else {
					double min = sval;
					int pos = -1;
					for(int m = 0; m < k; m++) {
						if(hash.get(top_k[m]) < min) {
							min = hash.get(top_k[m]);
							pos = m;
						}
					}
					if(pos != -1) {
						top_k[pos] = vns.get(j);
					}
				}
			}
			
			for(int j = 0; j < k; j++) {
				confuses.add(top_k[j]);
			}
			
			List<String> candidate = new ArrayList<String>();
			
			for (int j = 0; j < confuses.size(); j++) {
				//System.out.println( "iiiiiiiiiiii " + confuses.get(j));
				File f = new File(path + confuses.get(j) + ".m.gz");
				if(f.exists()) {
					//线性分类模型
					Linear classify = Linear.loadFrom(path + confuses.get(j) + ".m.gz");
					if(classify == null) {
						continue;
					}
					//System.out.println(one);
					SimpleFileReader reader = new SimpleFileReader(one, true, "test");
					
					Pipe lpipe = new Target2Label(al);
					Pipe fpipe = new StringArray2IndexArray(classify.getAlphabetFactory(), false);
					pipe = new SeriesPipes(new Pipe[] { lpipe, fpipe });
					
					test = new InstanceSet(pipe, classify.getAlphabetFactory());
					try {
						test.loadThruStagePipes_test(reader);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Instance inst = test.get(0);
					
					try {
						Results pred = (Results) classify.getInferencer().getBest(inst);
						//float l = loss.calc(pred.getPredAt(0), inst.getTarget());
						//System.out.println(pred.getPredAt(0));
						if (pred.getPredAt(0).toString().equals("1")) {
							continue;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					//System.out.println("ooooooooooo " +confuses.get(j));
					candidate.add(confuses.get(j));
				}
			}
			
			if(candidate.isEmpty()) {
				candidate.addAll(confuses);
				//candidate.sort(null);
			}
			candidate.add(now.get(i));
			List<String> candsents = build_candsents(candidate, i);
			
			int ans = usrsen.cal.computeProb(candsents);
			
			String best = "~~";
			if(ans < candidate.size()) {
				best = candidate.get(ans);
			}
			if(!now.get(i).equals(best))
			{
				String temp = "("+now.get(i)+") -> "+best;
				usrsen.results.add(temp);
			}
		}
	}
	
	static void test(UserSentence usrsen) throws Exception {

		vns = new ArrayList<String>();
		InputStreamReader in = null;
		//获取所有的搭配
		in = new InputStreamReader(new FileInputStream("E:\\STUDY\\JAVA\\collocation\\test\\all.txt"));
		BufferedReader bufferedReader = new BufferedReader(in);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			vns.add(lineTxt);
		}
		
		//ans为.tar.mz文件
		String ans = "E:\\STUDY\\JAVA\\collocation\\obj\\";
		
		//test_ins为特征文件的句子的集合
		List<String> test_ins = new ArrayList<String> ();
		test_ins=usrsen.own_features;
		//===============================
		
		now = new ArrayList<String>();
		now = usrsen.verbAndnone;
		test_sent = usrsen.str;

		/*props = new Properties();
		props.put("annotators", "tokenize,ssplit,pos, lemma");
		pipeline = new StanfordCoreNLP(props);
		lp = LexicalizedParser
				.loadModel("E:\\STUDY\\JAVA\\collocation\\src\\englishPCFG.ser.gz");*/
		
		evaluate(test_ins, usrsen,ans);
	}

	public static void main(String[] args) throws Exception {
		
	}
}
