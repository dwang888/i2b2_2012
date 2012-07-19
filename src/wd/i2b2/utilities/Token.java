/**
 * base class for tokens in data
 */
package wd.i2b2.utilities;

public class Token {

	String token;
	String stem;
	String text;
	int startOffset;
	int endOffset;
	int numChars;
	
	
	public int getNumChars(){
		return this.text.length();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}