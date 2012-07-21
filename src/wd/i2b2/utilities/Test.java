package wd.i2b2.utilities;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test {

	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 
	        Node nValue = (Node) nlList.item(0);
	 
		return nValue.getNodeValue();
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		String path = null;
		if(args.length >= 1){
			path = args[0];
		}
		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("TEXT");
		System.out.println(nList.getLength());
		
		for (int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
//			System.out.println(nNode.getAttributes().getNamedItem("start").getNodeName());
			System.out.println(nNode.getTextContent());
			if (nNode.getNodeType() == Node.ELEMENT_NODE){				 
			      Element eElement = (Element) nNode;
			      System.out.println("processing...");
			      
			      continue;
//			      System.out.println("First Name : " + getTagValue("firstname", eElement));
//			      System.out.println("Last Name : " + getTagValue("lastname", eElement));
//		          System.out.println("Nick Name : " + getTagValue("nickname", eElement));
//			      System.out.println("Salary : " + getTagValue("salary", eElement));
			   }
		}
		
		
	}

}
