package wd.i2b2.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sentence {

	String text;
	int numChars;	
	int numTokens = 0;
	List<Token> tokens;
	List<Event> events;
	List<Timex3> timex3s;
	List<Tlink> tlink;
	
	
	
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}