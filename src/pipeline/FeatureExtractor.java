/**
 * for extracting features from documents
 * */
package pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.mallet.types.Instance;

import wd.i2b2.utilities.DocumentI2b2;
import wd.i2b2.utilities.Event;
import wd.i2b2.utilities.Tlink;
import wd.i2b2.utilities.Timex3;
import wd.i2b2.utilities.Token;

public class FeatureExtractor {

	DocumentI2b2 doc;
	List<Tlink> dataSamplesTlink = new ArrayList<Tlink>();
	
	public FeatureExtractor(DocumentI2b2 d){
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
	
	public String getFeature_distance(Event e1, Event e2){
		if(Math.abs(e2.getStart() - e1.getStart()) < 50){
			return "close";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 100){
			return "middle";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 300){
			return "far";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 1000){
			return "more far";
		}else{
			return "superFar";
		}
	}
	
	public String getFeature_distance(Event e1, Timex3 e2){
		if(Math.abs(e2.getStart() - e1.getStart()) < 50){
			return "close";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 100){
			return "middle";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 300){
			return "far";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 1000){
			return "more far";
		}else{
			return "superFar";
		}
	}
	
	public String getFeature_distance(Timex3 e1, Timex3 e2){
		if(Math.abs(e2.getStart() - e1.getStart()) < 50){
			return "close";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 100){
			return "middle";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 300){
			return "far";
		}else if(Math.abs(e2.getStart() - e1.getStart()) < 1000){
			return "more far";
		}else{
			return "superFar";
		}
	}
	
	public String preposition(int start, int end, DocumentI2b2 doc){
		for(int i = 0; i < doc.getSentences().size(); i++){
			for(int j = 0; j < doc.getSentences().get(i).getTokens().size(); j++){
				Token tk = doc.getSentences().get(i).getTokens().get(j);
				if(tk.getPOS().equalsIgnoreCase("IN") && tk.getStartOffset() >= start && tk.getEndOffset() <= end){
//					System.out.println(tk.getText());
					return tk.getText();
				}
			}
		}
		return "NULL";
	}
	
	public void repeatPositiveSamples(DocumentI2b2 d){
		int num = d.getDataSamplesTlink().size();
		System.out.println(num);
		for(int i = 0; i < num; i++){
			Tlink t = d.getDataSamplesTlink().get(i);
			if(!t.getType().equalsIgnoreCase("NONE")){
//				System.out.println(t.getFromText());
				Tlink t2 = t.clone();
				d.getDataSamplesTlink().add(t2);
			}
		}
		
		System.out.println(d.getDataSamplesTlink().size()+ "-----------" + num);
	}
	
	public void generateMalletInstances(DocumentI2b2 d){
		this.repeatPositiveSamples(d);
		List<Tlink> allTlinks = d.getDataSamplesTlink();
		for(Tlink t : allTlinks){
			
			Iterator<String> itr = t.getFeatures().keySet().iterator();
			String key;
			String value;
			String fName;
			String feature = "";
			
			while(itr.hasNext()){
				key = itr.next();
				value = String.valueOf(t.getFeatures().get(key.trim()));
				fName = key + " " + value;
				fName = fName.replace(' ', '_');
				feature += fName + " ";
//				System.out.println(feature);
			}
			
			d.token2Instance(feature, t.getType(), "Anonymous", "unknow", d.getInsTlink());
			Instance origInstance = new Instance(feature, t.getType(), "Anonymous", "unknow");
//			System.out.println(feature);
			d.getOrigInstances().add(origInstance);
		}
		
	}
	
	public void extractFeatures(DocumentI2b2 d){
		this.doc = d;
		int numEvents = d.getEvents().size();
		int numTimex3s = d.getTimex3s().size();
		int numTlinks = d.getTlinks().size();
		String tlinkType = "NONE";
		Tlink tlinkTmp;
		
		//event to event
		for(int i = 0; i < numEvents-1; i++){
			for(int j = i + 1; j < numEvents; j++){
				Event ei = d.getEvents().get(i);
				Event ej = d.getEvents().get(j);
				
				tlinkTmp = this.findTlink(ei.getId(), ej.getId());
				if(tlinkTmp != null){
//					tlinkType = tlinkTmp.getType();
					tlinkTmp.getFeatures().put("fromText", tlinkTmp.getFromText());
					tlinkTmp.getFeatures().put("toText", tlinkTmp.getToText());					
				}else{
					if(this.getFeature_distance(ei, ej).equalsIgnoreCase("superFar"))continue;
					tlinkTmp = new Tlink(ei, ej);//a dummy tlink to create negative training sample
					tlinkTmp.setType("NONE");
					tlinkTmp.setId("T" + (d.getDataSamplesTlink().size()+1));
//					tlinkType = "NONE";
//					System.out.println(d.getEvents().get(i).getText());
					tlinkTmp.getFeatures().put("fromText", ei.getText());
					tlinkTmp.getFeatures().put("toText", ej.getText());
				}
				tlinkTmp.getFeatures().put("distance", this.getFeature_distance(ei, ej));
//				System.out.println(((Timex3)tlinkTmp.getFrom()).getText());
				tlinkTmp.getFeatures().put("PP", this.preposition(ei.getEnd(), ej.getStart(), doc));
				d.getDataSamplesTlink().add(tlinkTmp);//add tlink to tlink list, no matter true or false tlink
			}
		}
		
		//event to timex3
		for(int i = 0; i < numEvents-1; i++){
			for(int j = i + 1; j < numTimex3s; j++){
				tlinkTmp = this.findTlink(d.getEvents().get(i).getId(), d.getTimex3s().get(j).getId());
				if(tlinkTmp != null){
//					tlinkType = tlinkTmp.getType();
					tlinkTmp.getFeatures().put("fromText", tlinkTmp.getFromText());
					tlinkTmp.getFeatures().put("toText", tlinkTmp.getToText());
				}else{
					if(this.getFeature_distance(d.getEvents().get(i), d.getTimex3s().get(j)).equalsIgnoreCase("superFar"))continue;
					tlinkTmp = new Tlink(d.getEvents().get(i), d.getTimex3s().get(j));//a dummy tlink to create negative training sample
					tlinkTmp.setType("NONE");
					tlinkTmp.setId("T" + (d.getDataSamplesTlink().size()+1));
//					tlinkType = "NONE";
//					System.out.println(d.getEvents().get(i).getText());
					tlinkTmp.getFeatures().put("fromText", d.getEvents().get(i).getText());
					tlinkTmp.getFeatures().put("toText", d.getTimex3s().get(j).getText());
				}
				tlinkTmp.getFeatures().put("distance", this.getFeature_distance(d.getEvents().get(i), d.getTimex3s().get(j)));
//				System.out.println(((Timex3)tlinkTmp.getFrom()).getText());
				tlinkTmp.getFeatures().put("PP", this.preposition(d.getEvents().get(i).getEnd(), d.getTimex3s().get(j).getStart(), doc));
				d.getDataSamplesTlink().add(tlinkTmp);//add tlink to tlink list, no matter true or false tlink
				
			}
		}
		
		//timex3 to timex3
				for(int i = 0; i < numTimex3s-1; i++){
					for(int j = i + 1; j < numTimex3s; j++){
						tlinkTmp = this.findTlink(d.getTimex3s().get(i).getId(), d.getTimex3s().get(j).getId());
						if(tlinkTmp != null){
//							tlinkType = tlinkTmp.getType();
							tlinkTmp.getFeatures().put("fromText", tlinkTmp.getFromText());
							tlinkTmp.getFeatures().put("toText", tlinkTmp.getToText());
						}else{
							if(this.getFeature_distance(d.getTimex3s().get(i), d.getTimex3s().get(j)).equalsIgnoreCase("superFar"))continue;
							tlinkTmp = new Tlink(d.getTimex3s().get(i), d.getTimex3s().get(j));//a dummy tlink to create negative training sample
							tlinkTmp.setType("NONE");
							tlinkTmp.setId("T" + (d.getDataSamplesTlink().size()+1));
//							tlinkType = "NONE";
//							System.out.println(d.getEvents().get(i).getText());
							tlinkTmp.getFeatures().put("fromText", d.getTimex3s().get(i).getText());
							tlinkTmp.getFeatures().put("toText", d.getTimex3s().get(j).getText());
						}
						tlinkTmp.getFeatures().put("distance", this.getFeature_distance(d.getTimex3s().get(i), d.getTimex3s().get(j)));
//						System.out.println(((Timex3)tlinkTmp.getFrom()).getText());
						tlinkTmp.getFeatures().put("PP", this.preposition(d.getTimex3s().get(i).getEnd(), d.getTimex3s().get(j).getStart(), doc));
						d.getDataSamplesTlink().add(tlinkTmp);//add tlink to tlink list, no matter true or false tlink
						
					}
				}
		
		generateMalletInstances(d);//
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
