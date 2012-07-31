package UniTest;
import java.util.HashMap;


public class DataSample {

	/**
	 * @param args
	 */
	protected double distWithQuerry;
	protected String name;
	protected String label;
	protected HashMap<String, Integer> featureSet;
	
	public void setDist(double d){
		this.distWithQuerry = d;
	}
	
	public Double getDist(){
		return this.distWithQuerry;
	}
		
	public DataSample(){
		//dummy constructor
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setLabel(String l){
		this.label = l;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public void setFeatureSet(HashMap<String, Integer> fv){
		this.featureSet = fv;
	}
	
	public HashMap<String, Integer> getFeatureSet(){
		return this.featureSet;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
