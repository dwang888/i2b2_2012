/**
 * to laod the tag files created by Genia tagger
 * including: POS tagger, NE, shallow chunking
 * */

package wd.i2b2.dataIO;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wd.i2b2.utilities.Sentence;
import wd.i2b2.utilities.Token;

public class GeniaTagReader {

	String filePath;
	List<List<List<String>>> records = new ArrayList<List<List<String>>>();//keep the original record from file, sentence -> word -> attribute
	List<Sentence> sentences = new ArrayList<Sentence>();
	
	
	public GeniaTagReader() {
	}
	
	public GeniaTagReader(String path) throws IOException{
		this.filePath = path;
		this.loadFile(path);
		
	}
	
	
	public List<Sentence> getSentences(){
		return this.sentences;
	}
	
//	public String getPOS(List<String> attributes){
//		return attributes.get(2);
//	}
//	
//	public String getChunkBIO(List<String> attributes){
//		return attributes.get(3);
//	}
//	
//	public String getProteinBIO(List<String> attributes){
//		return attributes.get(4);
//	}
	
	public int getNumSents(){
		return this.records.size();
	}

	public void loadFile(String path) throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String line;
		List<List<String>> lines = new ArrayList<List<String>>();
		Sentence sent = new Sentence();
		List<String> attributes;
		int id = 0;
		
		while((line = br.readLine()) != null){
			line = line.trim();
			attributes = Arrays.asList(line.split("\\t"));
			sent.setText(line);
			if(line.length() == 0 && lines.size() != 0){
				this.records.add(lines);				
				this.sentences.add(sent);
				lines = new ArrayList<List<String>>();
				sent = new Sentence();
				id = 0;
			}else{
				lines.add(attributes);
				Token tk = new Token();
				tk.setId(id);
				id++;
				tk.setText(attributes.get(0));
//				System.out.println(line);
				tk.setPOS(attributes.get(2));
				tk.setChunkBIO(attributes.get(3));
				tk.setProteinBIO(attributes.get(4));				
				sent.addToken(tk);
			}
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path = args[0];
//		System.out.println(path);
		GeniaTagReader gr = new GeniaTagReader(path);
		
//		gr.loadFile(path);
		for(int i = 0; i < gr.getSentences().size(); i++){
//			System.out.println(gr.getSentences().get(i).getTokens().size());
			for(int j = 0; j < gr.getSentences().get(i).getTokens().size(); j++){
//				System.out.println(gr.getSentences().get(i).getTokens().get(j).getId());
			}
		}
	}

}
