package preprocess;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.DocumentException;

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
public class bnc_vn {
	private static HashMap<String, List<String> > table;
	private static List<String> vn_list; 
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
		String lemm_v = lemm(v);
		String lemm_n = lemm(n);
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
	public static void work(String filepath) throws DocumentException, IOException{
		InputStreamReader in = null;		
		in = new InputStreamReader(new FileInputStream(filepath));
		BufferedReader bufferedReader = new BufferedReader(in);
        String lineTxt = null;
        int no = 0;
        FileOutputStream log = null;
		log = new FileOutputStream(new File("F:\\eclipse\\BNC_1"));
        while((lineTxt = bufferedReader.readLine()) != null){
        	if(lineTxt.length() >= 30 && lineTxt.length() <= 150 && lineTxt.charAt(0) != 'бо'){
        		no++;
        		//System.out.println(lineTxt);
        		List<String> dobjs = dobj(lineTxt);
        		for(int i = 0; i < dobjs.size(); i++) {
        			String k = extract_vn(dobjs.get(i));
        			if(k != null) {
        				log.write((k + " " + lineTxt +"\r\n").getBytes());
        			}
        			//List<String> temp = table.get(k);
        			//if(temp == null) {
        				//	temp = new ArrayList<String>();
        			//}
        			//temp.add(lineTxt);
        			//table.put(k, temp);
        			//System.out.println(k);
        			//System.out.println(lineTxt);
        			//vn_list.add(k);
        		}
        		if(no % 100 == 0) { 
        			System.out.println(no);
        		}
        	}
            //System.out.println(lineTxt);
        }
		log.close();
        System.out.println(no);
	}
	
	public static void main(String[] args) throws Exception {
		table = new HashMap<String, List<String> >();
		vn_list = new ArrayList<String>();
		lp = LexicalizedParser.loadModel("englishPCFG.ser.gz");
		props = new Properties();
		props.put("annotators", "tokenize,ssplit,pos, lemma");
		pipeline = new StanfordCoreNLP(props);
		work("F:\\eclipse\\BNC");
		Iterator it = table.entrySet().iterator();
		/*while(it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			List<String> val = (List<String>) entry.getValue();
			if(val.size() >= 10) {
				FileOutputStream out = null;
				out = new FileOutputStream(new File("F:\\eclipse\\data\\" + entry.getKey() + ".txt"));
				for(int i = 0; i < val.size(); i++) {
					out.write((val.get(i) + "\r\n").getBytes() );
				}
				log.write((entry.getKey() + "  " + val.size() + "\r\n").getBytes() );
				out.close();
			}
		}*/
		
	}
}
