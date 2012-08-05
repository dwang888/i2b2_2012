/**
 * for loading the i2b2 xml file.
 * 
 * */

package wd.i2b2.dataIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wd.i2b2.utilities.Event;
import wd.i2b2.utilities.Sectime;
import wd.i2b2.utilities.Timex3;
import wd.i2b2.utilities.Tlink;


public class I2b2XmlReader {
	
	String path;
	Document doc;//this is a xml doc, not i2b2 Document
	String text = null;
	List<Event> events = new ArrayList<Event>();
	List<Tlink> tlinks = new ArrayList<Tlink>();
	List<Timex3> timex3s = new ArrayList<Timex3>();
	List<Sectime> sectimes = new ArrayList<Sectime>();

	
	public I2b2XmlReader(String path) throws Exception, IOException, Exception{
		this.loadI2b2xmlFile(path);
	}
	
	public void loadI2b2xmlFile(String path) throws ParserConfigurationException, Exception, IOException{
		File xmlFile = new File(path);		
		this.path = path;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		this.doc = dBuilder.parse(xmlFile);
		this.doc.getDocumentElement().normalize();
		this.readTexts();
		this.readEvents();
		this.readSectimes();
		this.readTimex3();
		this.readTlinks();
	}
	
	public String readTexts() throws Exception, IOException, Exception{
		if(this.doc == null){
			this.loadI2b2xmlFile(this.path);
		}
		NodeList nList = this.doc.getElementsByTagName("TEXT");
		String text = "";
		
		for (int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
//			System.out.println(nNode.getTextContent());
			text += nNode.getTextContent();
		}
		this.text = text;
		return text;
	}
	
	public void readEvents(){
		NodeList nList = doc.getElementsByTagName("EVENT");
		String attrName;
		String attrValue;
		
		for (int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
			Event event = new Event();
			
			for(int j = 0; j < nNode.getAttributes().getLength(); j++){
				//set attributes to event
				attrName = nNode.getAttributes().item(j).getNodeName();
				attrValue = nNode.getAttributes().item(j).getNodeValue();
				if(attrName.equalsIgnoreCase("id")){
					event.setId(attrValue);
				}else if(attrName.equalsIgnoreCase("start")){
					event.setStart(Integer.parseInt(attrValue));
				}else if(attrName.equalsIgnoreCase("end")){
					event.setEnd(Integer.parseInt(attrValue));
				}else if(attrName.equalsIgnoreCase("text")){
					event.setText(attrValue);
				}else if(attrName.equalsIgnoreCase("modality")){
					event.setModality(attrValue);
				}else if(attrName.equalsIgnoreCase("polarity")){
					event.setPolarity(attrValue);
				}else if(attrName.equalsIgnoreCase("type")){
					event.setType(attrValue);
				}
			}
			
			this.events.add(event);
		}
	}
	
	public void readTlinks(){
		NodeList nList = doc.getElementsByTagName("TLINK");
		String attrName;
		String attrValue;
		
		for (int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
			Tlink tlink = new Tlink();
			
			for(int j = 0; j < nNode.getAttributes().getLength(); j++){
				//set attributes to event
				attrName = nNode.getAttributes().item(j).getNodeName();
				attrValue = nNode.getAttributes().item(j).getNodeValue();
				if(attrName.equalsIgnoreCase("id")){
					tlink.setId(attrValue);
				}else if(attrName.equalsIgnoreCase("fromID")){
					tlink.setFromID(attrValue);
				}else if(attrName.equalsIgnoreCase("fromText")){
					tlink.setFromText(attrValue);
				}else if(attrName.equalsIgnoreCase("toID")){
					tlink.setToID(attrValue);
				}else if(attrName.equalsIgnoreCase("toText")){
					tlink.setToText(attrValue);
				}else if(attrName.equalsIgnoreCase("type")){
					tlink.setType(attrValue);
				}
			}			
			this.tlinks.add(tlink);
		}
	}
	
	public void readSectimes(){
		NodeList nList = doc.getElementsByTagName("SECTIME");
		String attrName;
		String attrValue;
		
		for (int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
			Sectime sectime = new Sectime();
			
			for(int j = 0; j < nNode.getAttributes().getLength(); j++){
				//set attributes to event
				attrName = nNode.getAttributes().item(j).getNodeName();
				attrValue = nNode.getAttributes().item(j).getNodeValue();
				if(attrName.equalsIgnoreCase("id")){
					sectime.setId(attrValue);
				}else if(attrName.equalsIgnoreCase("start")){
					sectime.setStart(Integer.parseInt(attrValue));
				}else if(attrName.equalsIgnoreCase("end")){
					sectime.setEnd(Integer.parseInt(attrValue));
				}else if(attrName.equalsIgnoreCase("text")){
					sectime.setText(attrValue);
				}else if(attrName.equalsIgnoreCase("type")){
					sectime.setType(attrValue);
				}else if(attrName.equalsIgnoreCase("dvalue")){
					sectime.setDvalue(attrValue);
				}
			}			
			this.sectimes.add(sectime);
			
		}
	}
	
	public void readTimex3(){
		NodeList nList = doc.getElementsByTagName("TIMEX3");
		String attrName;
		String attrValue;
		
		for (int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
			Timex3 timex3 = new Timex3();
			
			for(int j = 0; j < nNode.getAttributes().getLength(); j++){
				//set attributes to event
				attrName = nNode.getAttributes().item(j).getNodeName();
				attrValue = nNode.getAttributes().item(j).getNodeValue();
				if(attrName.equalsIgnoreCase("id")){
					timex3.setId(attrValue);
				}else if(attrName.equalsIgnoreCase("start")){
					timex3.setStart(Integer.parseInt(attrValue));
				}else if(attrName.equalsIgnoreCase("end")){
					timex3.setEnd(Integer.parseInt(attrValue));
				}else if(attrName.equalsIgnoreCase("text")){
					timex3.setText(attrValue);
				}else if(attrName.equalsIgnoreCase("type")){
					timex3.setType(attrValue);
				}else if(attrName.equalsIgnoreCase("val")){
					timex3.setVal(attrValue);
				}else if(attrName.equalsIgnoreCase("mod")){
					timex3.setMod(attrValue);
				}
			}			
			this.timex3s.add(timex3);
			
		}
	}
	
	
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<Tlink> getTlinks() {
		return tlinks;
	}

	public void setTlinks(List<Tlink> tlinks) {
		this.tlinks = tlinks;
	}

	public List<Timex3> getTimex3s() {
		return timex3s;
	}

	public void setTimex3s(List<Timex3> timex3s) {
		this.timex3s = timex3s;
	}

	public List<Sectime> getSectimes() {
		return sectimes;
	}

	public void setSectimes(List<Sectime> sectimes) {
		this.sectimes = sectimes;
	}

	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		String path = args[0];
		I2b2XmlReader reader = new I2b2XmlReader(path);
		reader.readSectimes();
//		System.out.println(reader.getSectimes().size());
	}

}
