/**
 * base class for tokens in data
 */
package wd.i2b2.utilities;

public class Token {

	int id;
//	String token;
	String stem;
	String text;
	int startOffset;
	int endOffset;
	int numChars;
	String POS;
	String chunkBIO;
	String proteinBIO;
	
	
	public int getNumChars(){
		return this.text.length();
	}
	
	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getStem() {
		return stem;
	}



	public void setStem(String stem) {
		this.stem = stem;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public int getStartOffset() {
		return startOffset;
	}



	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}



	public int getEndOffset() {
		return endOffset;
	}



	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}



	public void setNumChars(int numChars) {
		this.numChars = numChars;
	}



	public String getPOS() {
		return POS;
	}



	public void setPOS(String pOS) {
		POS = pOS;
	}



	public String getChunkBIO() {
		return chunkBIO;
	}



	public void setChunkBIO(String chunkBIO) {
		this.chunkBIO = chunkBIO;
	}



	public String getProteinBIO() {
		return proteinBIO;
	}



	public void setProteinBIO(String proteinBIO) {
		this.proteinBIO = proteinBIO;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}