package wd.i2b2.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sentence {

	String text;
	int numChars;	
	int numTokens = 0;
	List<Token> tokens = new ArrayList<Token>();
	List<Event> events;
	List<Timex3> timex3s;
	List<Tlink> tlinks;
	List<Sectime> sectimes;
	
	
	
	public Sentence(){
		
	}
	
	public int getNumTokens(){
		if(this.tokens == null){
			return 0;
		}else{
			return this.tokens.size();
		}		
	}
	
	public List<Token> getTokens(){
		return this.tokens;
	}
	
	public void addToken(Token tk){
		if(null == tk){
//			System.out.println("empty token!");
		}
//		System.out.println(tk.getText());
		this.tokens.add(tk);
	}
	
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNumChars() {
		return numChars;
	}

	public void setNumChars(int numChars) {
		this.numChars = numChars;
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

	public void setNumTokens(int numTokens) {
		this.numTokens = numTokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}