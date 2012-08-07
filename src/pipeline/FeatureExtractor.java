/**
 * for extracting features from documents
 * */
package pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wd.i2b2.utilities.Document;
import wd.i2b2.utilities.Event;
import wd.i2b2.utilities.Tlink;
import wd.i2b2.utilities.Timex3;

public class FeatureExtractor {

	Document doc;
	List<Tlink> dataSamplesTlink = new ArrayList<Tlink>();
	
	public FeatureExtractor(Document d){
		this.doc = d;
		this.extractFeatures(this.doc);
	}
	
	public Tlink findTlink(String fromID, String toID){
		for(Tlink t : this.doc.getTlinks()){
			if(t.getFromID().equalsIgnoreCase(fromID) 
					&& t.getToID().equalsIgnoreCase(toID)){
				return t;
			}
		}
		return null;
	}
	
	public int getFeature_distance(Event e1, Event e2){
		return Math.abs(e2.getStart() - e1.getStart());
	}
	
	public void generateMalletInstances(Document d){
		List<Tlink> allTlinks = d.getDataSamplesTlink();
		for(Tlink t : allTlinks){
			Iterator<String> itr = t.getFeatures().keySet().iterator();
			String key;
			String value;
			String fName;
			String feature = "";
			
			while(itr.hasNext()){
				key = itr.next();
				value = String.valueOf(d.getFeatures().get(key));
				fName = key + " " + value;
				fName = fName.replace(' ', '_');
				feature += fName + " ";
			}
			
			d.token2Instance(feature, t.getType(), "Anonymous", "unknow", d.getInsTlink());
		}
		
	}
	
	public void extractFeatures(Document d){
		this.doc = d;
		int numEvents = d.getEvents().size();
		int numTimex3s = d.getTimex3s().size();
		int numTlinks = d.getTlinks().size();
		String tlinkType = "NONE";
		Tlink tlinkTmp;
		
		//event to event
		for(int i = 0; i < numEvents-1; i++){
			for(int j = i + 1; j < numEvents; j++){
				tlinkTmp = this.findTlink(d.getEvents().get(i).getId(), d.getEvents().get(j).getId());
				if(tlinkTmp != null){
//					tlinkType = tlinkTmp.getType();
					tlinkTmp.getFeatures().put("fromText", tlinkTmp.getFromText());
					tlinkTmp.getFeatures().put("toText", tlinkTmp.getToText());
				}else{
					tlinkTmp = new Tlink(d.getEvents().get(i), d.getEvents().get(j));//a dummy tlink to create negative training sample
					tlinkTmp.setType("NONE");
//					tlinkType = "NONE";
					tlinkTmp.getFeatures().put("fromText", d.getEvents().get(i).getText());
					tlinkTmp.getFeatures().put("toText", d.getEvents().get(j).getText());
				}
				tlinkTmp.getFeatures().put("distance", this.getFeature_distance(d.getEvents().get(i), d.getEvents().get(j)));
//				System.out.println(((Timex3)tlinkTmp.getFrom()).getText());
				d.getDataSamplesTlink().add(tlinkTmp);//add tlink to tlink list, no matter true or false tlink
				
			}
		}
		generateMalletInstances(d);//
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
