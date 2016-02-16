package similarity;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class SimilarityCalculation {
	
	private static  ILexicalDatabase db = new NictWordNet();
	private static  RelatednessCalculator rc = new JiangConrath(db);
	
	public static double run( String word1, String word2 ) {
		WS4JConfiguration.getInstance().setMFS(true);
		String word1_v = word1.substring(0, word1.indexOf('_'));
		String word1_n = word1.substring(word1.indexOf('_') + 1);
		String word2_v = word2.substring(0, word2.indexOf('_'));
		String word2_n = word2.substring(word2.indexOf('_') + 1);
		double s1 = rc.calcRelatednessOfWords(word1_v, word2_v);
		double s2 = rc.calcRelatednessOfWords(word1_n, word2_n);
		return s1 + s2 ;
	}
	public static void main(String [] args) {
		System.out.println(run("learn_knowledge", "master_knowledge"));
		System.out.println(run("do_crime", "master_knowledge"));
	}
}