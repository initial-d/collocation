package preprocess;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import utils.StanfordParser;
import utils.lemma;
import utils.tools;
public class extract {
	private static HashMap<String, List<String> > table;
	private static List<String> table2;
	private static List<String> table1;
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
		String lemm_v = lemma.lemm(v);
		String lemm_n = lemma.lemm(n);
		if(lemm_v == null || lemm_n == null) {
			return null;
		}
		return lemm_v + "_" + lemm_n;
	}

	public static void work(String filepath) throws DocumentException, IOException{
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new File(filepath));
		Element root = doc.getRootElement();
		List childList = root.elements();
		Element head = root.element("head");
		Element text = head.element("text");
		String tot = "";
		for (Iterator it = text.elementIterator(); it.hasNext();) {
			Element answer = (Element) it.next();
			Element c = answer.element("coded_answer");
			for (Iterator iter = c.elementIterator(); iter.hasNext();) {
				Element e = (Element) iter.next();
				for (Iterator ns = e.elementIterator(); ns.hasNext();) {
					Element NS = (Element) ns.next();
					List ci = NS.elements();
					for (int i = 0; i < ci.size(); i++) {
						Element err = (Element) ci.get(i);
						String value = err.getName();
						if ("i".equals(value) && ci.size() > 1) {
							NS.remove(err);
						}
					}
				}
				if(tot != "" && (tot.charAt(tot.length() - 1) == '.' 
								|| tot.charAt(tot.length() - 1) == '?'
								|| tot.charAt(tot.length() - 1) == ','
								|| tot.charAt(tot.length() - 1) == '!')) {
					
					tot += " " + e.getStringValue();
				} else {
					tot += e.getStringValue();
				}
			}
		}
		
		
		SAXReader saxReader2 = new SAXReader();
		Document doc2 = saxReader2.read(new File(filepath));
		Element root2 = doc2.getRootElement();
		List childList2 = root2.elements();
		Element head2 = root2.element("head");
		Element text2 = head2.element("text");
		String tot2 = "";
		for (Iterator it = text2.elementIterator(); it.hasNext();) {
			Element answer = (Element) it.next();
			Element c = answer.element("coded_answer");
			for (Iterator iter = c.elementIterator(); iter.hasNext();) {
				Element e = (Element) iter.next();
				for (Iterator ns = e.elementIterator(); ns.hasNext();) {
					Element NS = (Element) ns.next();
					List ci = NS.elements();
					for (int i = 0; i < ci.size(); i++) {
						Element err = (Element) ci.get(i);
						String value = err.getName();
						if ("c".equals(value) && ci.size() > 1) {
							NS.remove(err);
						}
					}
				}
				if(tot2 != "" && (tot2.charAt(tot2.length() - 1) == '.' 
								|| tot2.charAt(tot2.length() - 1) == '?'
								|| tot2.charAt(tot2.length() - 1) == ','
								|| tot2.charAt(tot2.length() - 1) == '!')) {
					
					tot2 += " " + e.getStringValue();
				} else {
					tot2 += e.getStringValue();
				}
			}
		}
		//System.out.println(tot);
		//System.out.println(tot2);
		String[] sens = tools.SentenceDetect(tot);
		String[] sens2 = tools.SentenceDetect(tot2);
		//if(sens.length != sens2.length) {
		//	return;
		//}
		for(int i = 0; i < sens.length; i++) {
			table1.add(sens[i]);
		}
		for(int i = 0; i < sens2.length; i++) {
			table2.add(sens2[i]);
		}
	}
	static List<String> getFiles(String filePath) {
		List<String> ans = new ArrayList<String>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			File directory = new File(file.getAbsolutePath());
			File[] xmlFiles = directory.listFiles();
			for (File file1 : xmlFiles) {					
				ans.add(file1.getAbsolutePath());
			}
		}
		return ans;
	}
	public static boolean write_xml() throws IOException {
		FileOutputStream out = null;
		out = new FileOutputStream(new File("F:\\eclipse\\english_lm.txt"));
		FileOutputStream out2 = null;
		out2 = new FileOutputStream(new File("F:\\eclipse\\chinese_lm.txt"));
		for(int i = 0; i < table1.size(); i++) {
			out.write((table1.get(i) + "\r\n").getBytes() );
		}
		for(int i = 0; i < table2.size(); i++) {
			out2.write((table2.get(i) + "\r\n").getBytes() );
		}
		out.close();
		out2.close();
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		//work("F:\\eclipse\\fce-released-dataset\\fce-released-dataset\\dataset\\0100_2000_6\\doc2.xml");
		table = new HashMap<String, List<String> >();
		table1 = new ArrayList<String>();
		table2 = new ArrayList<String>();
		List<String> files = getFiles("F:\\eclipse\\fce-released-dataset\\fce-released-dataset\\dataset\\");
		for(int i = 0; i < files.size(); i++) {
			work(files.get(i));
		}
		if(write_xml()) {
			System.out.println("Y");
		}
		
		//extract_vn("dobj(do-4, What-1)");
	}
}