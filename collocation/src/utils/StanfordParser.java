package utils;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class StanfordParser {
	

/**Just for testing
 * @param args
 * @throws IOException 
 * @throws InvalidFormatException 
 */
/*public static void main(String[] args) throws IOException {
	LexicalizedParser lp = LexicalizedParser.loadModel("englishPCFG.ser.gz");
	  String subsen = "One beer later and I'm walking down the street smoking a cig with them";
	  PTBTokenizer ptb = PTBTokenizer.newPTBTokenizer(new StringReader(subsen));
	  List words = ptb.tokenize();
	  System.out.println(lp.parse(words));
}
*/
	
	public static void main(String[] args) {
	    //LexicalizedParser lp = LexicalizedParser.loadModel("D:\\my download\\Parser\\Stanford //Parser\\stanford-parser-full\\englishPCFG.ser.gz");
	   //���·������
	   LexicalizedParser lp = LexicalizedParser.loadModel("englishPCFG.ser.gz");
	   demoAPI(lp);
	   }
	 
	  public static void demoAPI(LexicalizedParser lp) {
	    // This option shows parsing a list of correctly tokenized words��һ��
	    //String[] sent = { "This", "is", "an", "easy", "sentence", "." };
	    //List rawWords = Sentence.toCoreLabelList(sent);
	    //Tree parse = lp.apply(rawWords);
	    //parse.pennPrint();
	    //System.out.println();
	    // This option shows loading and using an explicit tokenizer�ڶ���
	    String sent2 = "( However ) , I like eating red apples .";
	    TokenizerFactory tokenizerFactory =
	      PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    List rawWords2 =
	      tokenizerFactory.getTokenizer(new StringReader(sent2)).tokenize();
	    Tree parse = lp.apply(rawWords2);
	    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	    List tdl = gs.typedDependenciesCCprocessed();
	    System.out.println(tdl);
	    //for(TypedDependency tdl1:tdl){
	    //   System.out.println(tdl1);       //������������ģ�nsubj(sentence-4, This-1)
	    //   System.out.println(tdl1.gov()); //�������֧���λ�ģ�sentence-4
	    //   System.out.println(tdl1.dep()); //�������������λ�ģ�This-1
	    //   System.out.println(tdl1.reln());//���������ϵ��nsubj
	    //  }
	    System.out.println();
	    ////////////���������
	    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
	    tp.printTree(parse);
	  }
}