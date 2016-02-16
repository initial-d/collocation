package preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class script_rw {
	public static boolean read_write_xml(String filepath) throws IOException, DocumentException {
		HashMap<String, String> table = new HashMap<String, String>();
		InputStreamReader in = null;
		
		in = new InputStreamReader(new FileInputStream("F:\\eclipse\\confuse_collocation_15.txt"));
		BufferedReader bufferedReader = new BufferedReader(in);
        String lineTxt = null;
        while((lineTxt = bufferedReader.readLine()) != null){
            table.put(lineTxt, "yes");
        }
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new File(filepath));
		Element root = doc.getRootElement();
		List childList = root.elements();
		int count = 0 ;
		for (int i = 0; i < childList.size(); i++) {
			Element c = (Element) childList.get(i);
			System.out.println(c.attributeValue("v_n"));
			if(!table.containsKey(c.attributeValue("v_n"))) {
				childList.remove(i);
			} else {
				count++;
			}
		}
		
		XMLWriter writer = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		root.setAttributeValue("count", count + "");
		writer = new XMLWriter(new FileWriter("F:\\eclipse\\vn.xml"),
				format);
		writer.write(doc);
		writer.close();
		return true;
	}
	
	public static void main(String [] args) throws IOException, DocumentException {
		read_write_xml("F:\\eclipse\\vn_sens_15.xml");
		return;
	}
}
