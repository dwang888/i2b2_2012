package wd.i2b2.utilities;

import java.util.LinkedHashMap;
import java.util.Map;

public class Timex3 {
	
	String id;
	int start;
	int end;
	String text;
	String type;
	String val;
	String mod;
	Map<String, Integer> features = new LinkedHashMap<String, Integer>();
	
	public Timex3() {
	}
	
	public Timex3(String id, int start, int end, String text, String type,
			String val, String mod) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.text = text;
		this.type = type;
		this.val = val;
		this.mod = mod;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getMod() {
		return mod;
	}

	public void setMod(String mod) {
		this.mod = mod;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
