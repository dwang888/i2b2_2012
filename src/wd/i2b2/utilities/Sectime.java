package wd.i2b2.utilities;

public class Sectime {
	
	String id;
	int start;
	int end;
	String text;
	String type;
	String dvalue;
	
	public Sectime(){}	
	
	public Sectime(String id, int start, int end, String text, String type, String dvalue) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.text = text;
		this.type = type;
		this.dvalue = dvalue;
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

	public String getDvalue() {
		return dvalue;
	}

	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
