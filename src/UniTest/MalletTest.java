package UniTest;

import java.util.ArrayList;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.types.InstanceList;

public class MalletTest {


	public void importData(){
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		pipeList.add(new Target2Label());
		pipeList.add(new CharSequence2TokenSequence());
		pipeList.add(new TokenSequence2FeatureSequence());
		pipeList.add(new FeatureSequence2FeatureVector());
		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		instances.addThruPipe(new FileIterator(. . .));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MalletTest mt = new MalletTest();
		mt.importData();
	}

}
