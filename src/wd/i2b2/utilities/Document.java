package wd.i2b2.utilities;

import java.util.ArrayList;
import java.util.List;


public class Document {

	List<Sentence> sentences = new ArrayList<Sentence>();
	List<Event> events;
	List<Timex3> timex3s;
	List<Tlink> tlink;
	
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
