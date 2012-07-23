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

public class GeniaTagReader {

	String filePath;
	List<List<List<String>>> sentences = new ArrayList<List<List<String>>>();//keep the original record from file, sentence -> word -> attribute
	
	public GeniaTagReader() {
	}
	
	GeniaTagReader(String path) throws IOException{
		this.filePath = path;
		this.loadFile(path);
	}
	
	
	public List<List<List<String>>> getSentences(){
		return this.sentences;
	}
	
	public String getPOS(List<String> attributes){
		return attributes.get(2);
	}
	
	public String getChunkBIO(List<String> attributes){
		return attributes.get(3);
	}
	
	public String getProteinBIO(List<String> attributes){
		return attributes.get(4);
	}
	
	public int getNumSents(){
		return this.sentences.size();
	}

	public void loadFile(String path) throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String line;
		List<List<String>> sentence = new ArrayList<List<String>>();
		List<String> attributes;
		
		while((line = br.readLine()) != null){
			line = line.trim();
			attributes = Arrays.asList(line.split("\\t"));
			if(line.length() == 0 && sentence.size() != 0){
				this.sentences.add(sentence);
				sentence = new ArrayList<List<String>>();
			}else{
				sentence.add(attributes);
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
		System.out.println(path);
		GeniaTagReader gr = new GeniaTagReader();
		gr.loadFile(path);
	}

}
