package wd.i2b2.utilities;

import java.util.LinkedHashMap;
import java.util.Map;

public class Event {
	
	String id;
	int start;
	int end;
	String text;
	String modality;
	String polarity;
	String type;
	String sec_time_rel;
	Map<String, Integer> features = new LinkedHashMap<String, Integer>();
	
	public Event(){}
	
	public Event(String id, int start, int end, String text, String modality, String polarity, String type, String sec_time_rel){
		this.id = id;
		this.start = start;
		this.end = end;
		this.text = text;
		this.modality = modality;
		this.polarity = polarity;
		this.type = type;
		this.sec_time_rel = sec_time_rel;
	}
	
	public Event(String id, int start, int end, String text, String modality, String polarity, String type){
		this.id = id;
		this.start = start;
		this.end = end;
		this.text = text;
		this.modality = modality;
		this.polarity = polarity;
		this.type = type;
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

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getPolarity() {
		return polarity;
	}

	public void setPolarity(String polarity) {
		this.polarity = polarity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSec_time_rel() {
		return sec_time_rel;
	}

	public void setSec_time_rel(String sec_time_rel) {
		this.sec_time_rel = sec_time_rel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
