package UniTest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SaveDataInSource;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class MalletTest {
//	List<Instance> instances = new ArrayList<Instance>();
	InstanceList instances = this.buildPipe();
	
	public InstanceList buildPipe(){
		InstanceList instances = new InstanceList();
		ArrayList pipeList = new ArrayList();
		pipeList = new ArrayList();
		
		pipeList.add(new Target2Label());
//		pipeList.add(new SaveDataInSource());
		Pattern tokenPattern = Pattern.compile("[\\w=_]+");
        // Tokenize raw strings
		pipeList.add(new CharSequence2TokenSequence(tokenPattern));
//		pipeList.add(new TokenSequence2FeatureVectorSequence());
		pipeList.add(new TokenSequence2FeatureSequence());        
		pipeList.add(new FeatureSequence2FeatureVector());
		pipeList.add(new PrintInputAndTarget());
        instances = new InstanceList(new SerialPipes(pipeList));
        return instances;
	}
	
	public Instance token2Instance(String feature){
//		for(String key : tk.features.keySet()){
//			feature += key + "=" + tk.features.get(key) + " ";
//		}
		Instance in = new Instance(feature, "positive", "anyone", "anywhere");
//		System.out.println(in.getName());
//		System.out.println(featureData);
//		System.out.println(feature);
//		System.out.println(in.getTarget());
//		System.out.println(in.getName().toString());
		//return in;
		this.instances.addThruPipe(in);
		return this.instances.get(this.instances.size() - 1);
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MalletTest mt = new MalletTest();
		mt.token2Instance("f1 f2 f3");
	}

}
