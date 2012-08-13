package wd.i2b2.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import wd.i2b2.dataIO.DepReader;
import wd.i2b2.dataIO.GeniaTagReader;
import wd.i2b2.dataIO.I2b2XmlReader;

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

public class DocumentI2b2 {

	String xmlPath;
	List<Sentence> sentences;
	List<Event> events;
	List<Timex3> timex3s;
	List<Tlink> tlinks;
	String text;
	Map<String, Integer> features = new LinkedHashMap<String, Integer>();
	List<Tlink> dataSamplesTlink = new ArrayList<Tlink>();
	InstanceList insTlink;
	List<Instance> origInstances = new ArrayList<Instance>();
	Pipe finalPipe;
	
	public DocumentI2b2(String xmlPath, String geniaPath, String depPath) throws Exception{
		I2b2XmlReader xmlReader = new I2b2XmlReader(xmlPath);
		if(geniaPath!= null && !geniaPath.equalsIgnoreCase("")){
			GeniaTagReader geniaReader = new GeniaTagReader(geniaPath);
			this.sentences = geniaReader.getSentences();
		}
		if(depPath!= null && !depPath.equalsIgnoreCase("")){
			DepReader depReader = new DepReader(depPath);
		}
		
		
		this.xmlPath = xmlPath;
		this.events = xmlReader.getEvents();
		this.timex3s = xmlReader.getTimex3s();
		this.tlinks = xmlReader.getTlinks();
		this.text = xmlReader.getText();		
		this.assignTokenOffset();		
		this.buildPipe();
		this.insTlink = new InstanceList(finalPipe);
	}
	
//	public Document(String xmlPath, String geniaPath){
//		this.Document(xmlPath, geniaPath, "");
//	}
	
	public DocumentI2b2(){
		
	}
	
	
	public Pipe buildPipe(){
		ArrayList pipeList = new ArrayList();		
		pipeList.add(new Target2Label());
//		pipeList.add(new SaveDataInSource());//a dummy tlink to create negative training sample
		Pattern tokenPattern = Pattern.compile("[\\w=_]+");
		pipeList.add(new CharSequence2TokenSequence(tokenPattern));
		pipeList.add(new TokenSequence2FeatureSequence());        
		pipeList.add(new FeatureSequence2FeatureVector());
		Pipe finalPipe = new SerialPipes(pipeList);
		this.finalPipe = finalPipe;
		return finalPipe;
	}
	
	public Instance token2Instance(String feature, String label, String name, String src, InstanceList ins){
		Instance in = new Instance(feature, label, name, src);
		ins.addThruPipe(in);
		return ins.get(ins.size() - 1);
	}
	
	/**
	 * assign char start and end offset to token from text
	 * must run in the end of constructor
	 * */
	public void assignTokenOffset(){
		int startOffset = 1;
		int tkID = 0;
		int sentID = 0;
		for(int i = 1; i < this.getText().length(); i++){
			
			if(this.getText().charAt(i) == ' '){
				if(i >= startOffset){
					this.getSentences().get(sentID).getTokens().get(tkID).setEndOffset(i);
					this.getSentences().get(sentID).getTokens().get(tkID).setStartOffset(startOffset);
				}
//				System.out.println(startOffset + "--" + i + "--" + this.getSentences().get(sentID).getTokens().get(tkID).getText());
				startOffset = i;
				while(startOffset + 1 < this.getText().length()){
					startOffset++;
					if(this.getText().charAt(startOffset) != ' ' && this.getText().charAt(startOffset) != '\n'){
//						if(this.getText().charAt(startOffset) == '\n'){
//							break;
//						}
						if(this.getText().charAt(i-1) != ' '){
							tkID++;
						}						
						break;
					}
				}							
			}else if(this.getText().charAt(i) == '\n'){
//				System.out.println(this.getSentences().get(sentID).getTokens().size() + "=== " +tkID);
//				this.getSentences().get(sentID).getTokens();
				if(i >= startOffset){
					this.getSentences().get(sentID).getTokens().get(tkID).setEndOffset(i);
					this.getSentences().get(sentID).getTokens().get(tkID).setStartOffset(startOffset);
				}
				startOffset = i;
				while(startOffset + 1 < this.getText().length()){
					startOffset++;
					if(this.getText().charAt(startOffset) != ' ' && this.getText().charAt(startOffset) != '\n'){
						tkID = 0;
						break;
					}
					
				}
				
				sentID++;	
			}
			
			
			
//			if(startOffset >= this.getText().length() 
//					|| sentID >= this.getSentences().size()){
			if(startOffset >= this.getText().length() || sentID >= this.getSentenceNum()){
//				System.out.println("startOffset:\t" + startOffset);
//				System.out.println("sentID:\t" + sentID);
//				System.out.println("this.getSentences().size():\t" + this.getSentences().size());
//				System.out.println("tkID:\t" + tkID);
//				System.out.println("this.getSentences().get(sentID).getTokens().size():\t" + this.getSentences().get(sentID).getTokens().size());
								
				break;
			}
//			System.out.println(this.getSentences().size() + " " + sentID);
//			
//			if(tkID >= this.getSentences().get(sentID).getNumTokens()){
//				//deal with some continuous space
//				tkID = 0;				
//			}
		}
//		System.out.println("text length:\t" + this.getText().length());
	}
	
	public int getNumSents(){
		return this.sentences.size();
	}
	
	public int getNumTokens(){
		if(this.sentences == null){
			return 0;
		}else{
			int num = 0;
			for(Sentence s : this.sentences){
				num += s.getNumTokens();
			}
			return num;
		}
	}
	
	
	
	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<Timex3> getTimex3s() {
		return timex3s;
	}

	public void setTimex3s(List<Timex3> timex3s) {
		this.timex3s = timex3s;
	}

	public List<Tlink> getTlinks() {
		return tlinks;
	}

	public void setTlinks(List<Tlink> tlinks) {
		this.tlinks = tlinks;
	}
	
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Tlink findTlink(String fromID, String toID){
		for(Tlink t : this.getTlinks()){
			if(t.getFromID().equalsIgnoreCase(fromID) 
					&& t.getToID().equalsIgnoreCase(toID)){
				return t;
			}
		}
		return null;
	}
	
	public Event findEvent(String id){
		for(Event e : this.getEvents()){
			if(id.equalsIgnoreCase(e.getId())){
				return e;
			}
		}
		return null;
	}
	
	public Timex3 findTimex3(String id){
		for(Timex3 t : this.getTimex3s()){
			if(id.equalsIgnoreCase(t.getId())){
				return t;
			}
		}
		return null;
	}
	

	public Map<String, Integer> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, Integer> features) {
		this.features = features;
	}

	public List<Tlink> getDataSamplesTlink() {
		return dataSamplesTlink;
	}

	public void setDataSamplesTlink(List<Tlink> dataSamplesTlink) {
		this.dataSamplesTlink = dataSamplesTlink;
	}
	
	

	public InstanceList getInsTlink() {
		return insTlink;
	}

	public void setInsTlink(InstanceList insTlink) {
		this.insTlink = insTlink;
	}

	public Pipe getFinalPipe() {
		return finalPipe;
	}

	public void setFinalPipe(Pipe finalPipe) {
		this.finalPipe = finalPipe;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public List<Instance> getOrigInstances() {
		return origInstances;
	}

	public void setOrigInstances(List<Instance> origInstances) {
		this.origInstances = origInstances;
	}
	
	public int getSentenceNum(){
		return this.getText().split("\n").length;
	}
	
	/**
	 * read in a xml file, append the tlinks to TAGS
	 * */
	public void output2XML(String inputPath, String outputPath) throws Exception, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		//read and edit xml
		File inputFile = new File(inputPath);
		Document document = builder.parse(inputFile);
		System.out.println("manipulating xml file:\t" + inputPath);
		
		Node tags = document.getElementsByTagName("TAGS").item(0);
		
		for(Tlink t : this.getDataSamplesTlink()){
			if(!t.getType().equalsIgnoreCase("NONE")){
				//a link by classifier
				Element newTlink = document.createElement("TLINK");
				newTlink.setAttribute("id", t.getId());
				newTlink.setAttribute("fromID", t.getFromID());
				newTlink.setAttribute("fromText", t.getFromText());
				newTlink.setAttribute("toID", t.getToID());
				newTlink.setAttribute("toText", t.getToText());
				newTlink.setAttribute("type", t.getType());
				tags.appendChild(newTlink);				
			}
		}		
//		System.out.println(document.getElementsByTagName("TLINK").getLength());
		
		//transformer
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(outputPath));
		transformer.transform(source, result);
		System.out.println("reading, transforming and writing xml done!");

}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String xmlPath = "D:\\projects\\i2b2_2012\\data\\2012-07-06.release-fix\\8.xml";
		String geniaPath = "D:\\projects\\i2b2_2012\\data\\2012-07-06_genia\\8.xml.txt.genia";
		String depPath = "D:\\projects\\i2b2_2012\\data\\2012-07-06_depfix\\8.xml.parse";

		DocumentI2b2 d = new DocumentI2b2();
		d.output2XML(args[0], args[1]);
		System.exit(0);
		
		DocumentI2b2 doc = new DocumentI2b2(xmlPath, geniaPath, depPath);
		System.out.println("-----------------");
		System.out.println(doc.getText());
		System.out.println("-----------------");
		for(int i =0; i < doc.getSentences().size(); i++){
			System.out.println("---------------------------------" + doc.getSentences().get(i).getNumTokens());
			for(int j = 0; j < doc.getSentences().get(i).getNumTokens(); j++){
				System.out.println(doc.getSentences().get(i).getTokens().get(j).getText());
				System.out.println(doc.getSentences().get(i).getTokens().get(j).getStartOffset());
				System.out.println(doc.getSentences().get(i).getTokens().get(j).getEndOffset());
			
			}
		}
	}

}
