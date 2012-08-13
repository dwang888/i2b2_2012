package wd.i2b2.utilities;

import java.util.LinkedHashMap;
import java.util.Map;

import cc.mallet.types.Instance;

public class Tlink {
	
	String id;
	String fromID;
	String fromText;
	String toID;
	String toText;
	String type;
	Map<String, Object> features = new LinkedHashMap<String, Object>();
	Instance instanceTlink;
	Object from;
	Object to;
	
	public Tlink(){}
	
	public Tlink(Event f, Event t){
		this.from = f;
		this.to = t;
		this.setFromID(f.getId());
		this.setFromText(f.getText());
		this.setToID(t.getId());
		this.setToText(t.getText());
	}
	
	public Tlink(Timex3 f, Timex3 t){
		this.from = f;
		this.to = t;
		this.setFromID(f.getId());
		this.setFromText(f.getText());
		this.setToID(t.getId());
		this.setToText(t.getText());
	}
	
	public Tlink(Event f, Timex3 t){
		this.from = f;
		this.to = t;
		this.setFromID(f.getId());
		this.setFromText(f.getText());
		this.setToID(t.getId());
		this.setToText(t.getText());
	}
	
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

	public Tlink clone(){
		return new Tlink(id, fromID, fromText, toID, toText, type);
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
	
	

	public Map<String, Object> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, Object> features) {
		this.features = features;
	}

	
	
	public Instance getInstanceTlink() {
		return instanceTlink;
	}

	public void setInstanceTlink(Instance instanceTlink) {
		this.instanceTlink = instanceTlink;
	}

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
