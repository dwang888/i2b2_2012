package wd.i2b2.utilities;

public class Tlink {
	
	String id;
	String fromID;
	String fromText;
	String toID;
	String toText;
	String type;
	
	
	
	public Tlink(){}
	
	public Tlink(String id, String fromID, String fromText, String toID,
			String toText, String type) {
		super();
		this.id = id;
		this.fromID = fromID;
		this.fromText = fromText;
		this.toID = toID;
		this.toText = toText;
		this.type = type;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromID() {
		return fromID;
	}

	public void setFromID(String fromID) {
		this.fromID = fromID;
	}

	public String getFromText() {
		return fromText;
	}

	public void setFromText(String fromText) {
		this.fromText = fromText;
	}

	public String getToID() {
		return toID;
	}

	public void setToID(String toID) {
		this.toID = toID;
	}

	public String getToText() {
		return toText;
	}

	public void setToText(String toText) {
		this.toText = toText;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
