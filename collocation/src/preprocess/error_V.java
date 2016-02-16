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

public class error_V {
	private static HashMap<String, String> table;
	static FileOutputStream out = null;

	public static void work(String filepath) throws DocumentException,
			IOException {
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new File(filepath));
		Element root = doc.getRootElement();
		List childList = root.elements();
		Element head = root.element("head");
		Element text = head.element("text");
		for (Iterator it = text.elementIterator(); it.hasNext();) {
			Element answer = (Element) it.next();
			Element c = answer.element("coded_answer");
			
			for (Iterator iter = c.elementIterator(); iter.hasNext();) {
				Element e = (Element) iter.next();
				boolean flag = false;
				List<String> C = new ArrayList<String>();
				List<String> I = new ArrayList<String>();
				
				for (Iterator ns = e.elementIterator(); ns.hasNext();) {
					Element NS = (Element) ns.next();
					System.out.println(NS.attributeValue("type"));
					
					if (NS.attributeValue("type").equals("RV")
							|| NS.attributeValue("type").equals("DV")
							|| NS.attributeValue("type").equals("FV")
							|| NS.attributeValue("type").equals("CL")) {
						out.write((NS.getStringValue() + "\r\n").getBytes());
						flag = true;
						C.add(NS.elementText("c"));
						I.add(NS.elementText("i"));
						List ci = NS.elements();
						for (int i = 0; i < ci.size(); i++) {
							Element err = (Element) ci.get(i);
							String value = err.getName();
							if ("c".equals(value)) {
								NS.remove(err);
							}
						}
					} else {
						List ci = NS.elements();
						for (int i = 0; i < ci.size(); i++) {
							Element err = (Element) ci.get(i);
							String value = err.getName();
							if ("i".equals(value)) {
								NS.remove(err);
							}
						}
					}
				}
				if (flag) {
					String p = e.getStringValue();
					String[] sens = tools.SentenceDetect(p);
					for (int m = 0; m < I.size(); m++) {
						for (int i = 0; i < sens.length; i++) {
							if (sens[i] != null && !I.isEmpty() && sens[i].contains(I.get(m))) {
								String key = I.get(m) + " / " + C.get(m);
								table.put(key, sens[i]);
							}
						}
					}
				}
			}
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
		out = new FileOutputStream(new File("F:\\eclipse\\correction2.txt"));
		Iterator it = table.keySet().iterator();
		XMLWriter writer = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		Document doc = DocumentHelper.createDocument();
		Element vn = doc.addElement("vn");
		int count = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			String val = table.get(key);
			count++;
			Element coll = vn.addElement("correction");
			out.write((key + "\r\n").getBytes());
			Element sen = coll.addElement("sentence");
			sen.setText(val);
		}
		vn.addAttribute("count", count + "");
		writer = new XMLWriter(new FileWriter("F:\\eclipse\\correction.xml"),
				format);
		writer.write(doc);
		writer.close();
		return true;
	}

	public static void main(String[] args) throws Exception {
		// work("F:\\eclipse\\fce-released-dataset\\fce-released-dataset\\dataset\\0100_2000_6\\doc2.xml");
		table = new HashMap<String, String>();
		List<String> files = getFiles("F:\\eclipse\\fce-released-dataset\\fce-released-dataset\\dataset\\");
		out = new FileOutputStream(new File("F:\\eclipse\\correction.txt"));
		for (int i = 0; i < files.size(); i++) {
			work(files.get(i));
		}
		if (write_xml()) {
			System.out.println("Y");
		}
		// extract_vn("dobj(do-4, What-1)");
	}
}