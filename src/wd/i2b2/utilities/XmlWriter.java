/**
 * test xml writing
 * */
package wd.i2b2.utilities;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlWriter {

	public void processXML(String inputPath, String outputPath) throws Exception, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		//read and edit xml
		File inputFile = new File(inputPath);
		Document document = builder.parse(inputFile);
		System.out.println("processing file:\t" + inputPath);
		
		Node tags = document.getElementsByTagName("TAGS").item(0);
		Element newTlink = document.createElement("NEWTLINK");
		newTlink.setAttribute("fromID", "E1");
		newTlink.setAttribute("fromText", "text for from text");
		newTlink.setAttribute("id", "T1");
		newTlink.setAttribute("toID", "E2");
		newTlink.setAttribute("toText", "text for to text");
		newTlink.setAttribute("type", "type1");
		
		tags.appendChild(newTlink);
		
		newTlink = document.createElement("NEWTLINK");
		newTlink.setAttribute("fromID", "E1");
		newTlink.setAttribute("fromText", "text for from text");
		newTlink.setAttribute("id", "T1");
		newTlink.setAttribute("toID", "E2");
		newTlink.setAttribute("toText", "text for to text");
		newTlink.setAttribute("type", "type1");
		tags.appendChild(newTlink);
		tags.appendChild(newTlink);
		
		System.out.println(document.getElementsByTagName("TLINK").getLength());
		
		//transformer
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(outputPath));
		transformer.transform(source, result);
		System.out.println("transforming and writing done!");
		
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, Exception {
		String inputPath = args[0];
		String outputPath = args[1];
		
		XmlWriter w = new XmlWriter();
		w.processXML(inputPath, outputPath);
		

	}

}
