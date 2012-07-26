package wd.i2b2.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wd.i2b2.dataIO.DepReader;
import wd.i2b2.dataIO.GeniaTagReader;
import wd.i2b2.dataIO.I2b2XmlReader;


public class Document {

	List<Sentence> sentences;
	List<Event> events;
	List<Timex3> timex3s;
	List<Tlink> tlinks;
	String text;
	
	
	public Document(String xmlPath, String geniaPath, String depPath) throws Exception{
		I2b2XmlReader xmlReader = new I2b2XmlReader(xmlPath);
		GeniaTagReader geniaReader = new GeniaTagReader(geniaPath);
		DepReader depReader = new DepReader(depPath);
		
		this.events = xmlReader.getEvents();
		this.timex3s = xmlReader.getTimex3s();
		this.tlinks = xmlReader.getTlinks();
		this.text = xmlReader.getText();
		this.sentences = geniaReader.getSentences();		
		this.assignTokenOffset();
	}
	
//	public Document(String xmlPath, String geniaPath){
//		this.Document(xmlPath, geniaPath, "");
//	}
	
	public Document(){
		
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
//				System.out.println("%");				
				this.getSentences().get(sentID).getTokens().get(tkID).setEndOffset(i);
				this.getSentences().get(sentID).getTokens().get(tkID).setStartOffset(startOffset);
				startOffset = i + 1;
				tkID++;				
			}else if(this.getText().charAt(i) == '\n'){
				System.out.println(this.getSentences().get(sentID).getTokens().size() + "=== " +tkID);
				this.getSentences().get(sentID).getTokens().get(tkID).setEndOffset(i);
				this.getSentences().get(sentID).getTokens().get(tkID).setStartOffset(startOffset);
				startOffset = i + 1;				
				tkID = 0;
				sentID++;				
//					System.out.println(this.getText().charAt(i));		
			}
			
			if(startOffset >= this.getText().length() 
					|| sentID >= this.getSentences().size() 
					|| tkID >= this.getSentences().get(sentID).getTokens().size()){
				break;
			}
		}
		
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

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String xmlPath = "D:\\projects\\i2b2_2012\\data\\2012-06-18.release-fix\\11.xml";
		String geniaPath = "D:\\projects\\i2b2_2012\\data\\2012-06-18_genia\\11.xml.txt.genia";
		String depPath = "D:\\projects\\i2b2_2012\\data\\2012-06-18_depfix\\11.xml.parse";

		Document doc = new Document(xmlPath, geniaPath, depPath);
		System.out.println("-----------------");
		System.out.println(doc.getText());
		System.out.println("-----------------");
		for(int i =0; i < doc.getSentences().size(); i++){
			System.out.println("---------------------------------");
			for(int j = 0; j < doc.getSentences().get(i).getNumTokens(); j++){
				System.out.println(doc.getSentences().get(i).getTokens().get(j).getText());
				System.out.println(doc.getSentences().get(i).getTokens().get(j).getStartOffset());
				System.out.println(doc.getSentences().get(i).getTokens().get(j).getEndOffset());
			
			}
		}
	}

}
