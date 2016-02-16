package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**a Shallow Parser based on opennlp
 * @author yangliu
 * @blog http://blog.csdn.net/yangliuy
 * @mail yang.liu@pku.edu.cn
 */

public class ShallowParser {
	
	private static ShallowParser instance = null ;
	private static POSModel model;
	private static ChunkerModel cModel ;
	
	//Singleton pattern
	public static ShallowParser getInstance() throws InvalidFormatException, IOException{
		if(ShallowParser.instance == null){
			POSModel model = new POSModelLoader().load(new File("F:\\workspace\\collocation\\src\\en-pos-maxent.bin"));
			InputStream is = new FileInputStream("F:\\workspace\\collocation\\src\\en-chunker.bin");
			ChunkerModel cModel = new ChunkerModel(is);
			ShallowParser.instance = new ShallowParser(model, cModel);
		}
		return ShallowParser.instance;
	}
	
	public ShallowParser(POSModel model, ChunkerModel cModel){
		ShallowParser.model = model;
		ShallowParser.cModel = cModel;
		
	}
	
	 /** A shallow Parser, chunk a sentence and return a map for the phrase
	  *  labels of words <wordsIndex, phraseLabel>
	 *   Notice: There should be " " BEFORE and after ",", " ","(",")" etc.
	 * @param input The input sentence
	 * @param model The POSModel of the chunk
	 * @param cModel The ChunkerModel of the chunk
	 * @return  HashMap<Integer,String>
	 */
	 public HashMap<Integer,String> chunk(String input) throws IOException { 	
			PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
			POSTaggerME tagger = new POSTaggerME(model);
			ObjectStream<String> lineStream = new PlainTextByLineStream(
					new StringReader(input));
			perfMon.start();
			String line;
			String whitespaceTokenizerLine[] = null; 
			String[] tags = null;
			while ((line = lineStream.read()) != null) {
				whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
						.tokenize(line);
				tags = tagger.tag(whitespaceTokenizerLine);	 
				POSSample posTags = new POSSample(whitespaceTokenizerLine, tags);
				System.out.println(posTags.toString());
				perfMon.incrementCounter();
			}
			perfMon.stopAndPrintFinalResult();
	 
			// chunker
			ChunkerME chunkerME = new ChunkerME(cModel);
			String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);
			
			HashMap<Integer,String> phraseLablesMap = new HashMap<Integer, String>();
			Integer wordCount = 1;
			Integer phLableCount = 0;
			for (String phLable : result){
				if(phLable.equals("O")) phLable += "-Punctuation"; //The phLable of the last word is OP
				if(phLable.split("-")[0].equals("B")) phLableCount++;
				phLable = phLable.split("-")[1] + phLableCount;
				//if(phLable.equals("ADJP")) phLable = "NP"; //Notice: ADJP included in NP
				//if(phLable.equals("ADVP")) phLable = "VP"; //Notice: ADVP included in VP
				System.out.println(wordCount + ":" + phLable);
				phraseLablesMap.put(wordCount, phLable);
				wordCount++;
			}
				
			//Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
			//for (Span phLable : span)
				//System.out.println(phLable.toString());
			return phraseLablesMap;
		}
	 
	 /** Just for testing
		 * @param tdl Typed Dependency List
		 * @return WDTreeNode root of WDTree
		 */
	 public static void main(String[] args) throws IOException {
		 //Notice: There should be " " BEFORE and after ",", " ","(",")" etc.
		 String input = "We really enjoyed using the Canon PowerShot SD500 .";
		 //String input = "Bell , based in Los Angeles , makes and distributes electronic , computer and building products .";
		 ShallowParser swParser = ShallowParser.getInstance();
		 swParser.chunk(input);
	 }
	     
}
