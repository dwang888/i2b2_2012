package UniTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
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
//		pipeList.add(new PrintInputAndTarget());
        instances = new InstanceList(new SerialPipes(pipeList));
        return instances;
	}
	
	public Instance token2Instance(String feature, String label, String name, String src, InstanceList instances){
//		for(String key : tk.features.keySet()){
//			feature += key + "=" + tk.features.get(key) + " ";
//		}
		Instance in = new Instance(feature, label, name, src);
//		System.out.println(in.getName());
//		System.out.println(featureData);
//		System.out.println(feature);
//		System.out.println(in.getTarget());
//		System.out.println(in.getName().toString());
		//return in;
		instances.addThruPipe(in);
		return instances.get(instances.size() - 1);
	}
	
	public LinkedList<DataSample> createSampleList(String fileName) throws IOException{
		File file = new File(fileName);
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		LinkedList<DataSample> samples = new LinkedList<DataSample>();
		
		//convert each line to feature vector
		while((line = br.readLine()) != null){
			String[] lineSplit = line.split(" ");
			DataSample dstmp = new DataSample();
			dstmp.setName(lineSplit[0]);
			dstmp.setLabel(lineSplit[1]);
			HashMap<String, Integer> fvtmp = new HashMap<String, Integer>();
			for(int i = 2; i < lineSplit.length; i += 2){
				String featureName = lineSplit[i];
				int featureValue = Integer.parseInt(lineSplit[i+1]);
				fvtmp.put(featureName, featureValue);				
			}
			dstmp.setFeatureSet(fvtmp);
			samples.add(dstmp);
		}	
		br.close();
		return samples;
	}
	
	public void process(String trainPath, String testPath) throws IOException{
		List<DataSample> trainList = this.createSampleList(trainPath);
		List<DataSample> testList = this.createSampleList(testPath);
		InstanceList instancesTrain = this.buildPipe();
		InstanceList instancesTest = this.buildPipe();
		
		
		for(DataSample ds : trainList){
			String strTmp = "";
			String[] words = (String[]) ds.featureSet.keySet().toArray(new String[0]);
			for(int i = 0; i < words.length; i++){
				strTmp += " " + words[i];
//				strTmp += " " + words[i]+"="+ds.featureSet.get(words[i]);
			}			
//			System.out.println(strTmp);
//			System.out.println(ds.getLabel());
			token2Instance(strTmp, ds.getLabel(), ds.getName(), "NONE", instancesTrain);
		}
		
		for(DataSample ds : testList){
			String strTmp = "";
			String[] words = (String[]) ds.featureSet.keySet().toArray(new String[0]);
			for(int i = 0; i < words.length; i++){
				strTmp += " " + words[i];
//				strTmp += " " + words[i]+"="+ds.featureSet.get(words[i]);
			}			
//			System.out.println(strTmp);
//			System.out.println(ds.getLabel());
			token2Instance(strTmp, ds.getLabel(), ds.getName(), "NONE", instancesTest);
		}
		
		ClassifierTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(instancesTrain);
		System.out.println(classifier.getF1(instancesTest, 1));
//		Trial testTrial = new Trial(classifier, instancesTest);
//		classifier.print();
//		printTrialClassification(testTrial);
//		System.out.println(testTrial.getAccuracy());
	}
	
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String trainPath = args[0];
		String testPath = args[1];
		MalletTest mt = new MalletTest();
//		mt.token2Instance("f1 f2 f3");
		List<DataSample> trainList = mt.createSampleList(trainPath);
		List<DataSample> testList = mt.createSampleList(testPath);
		mt.process(trainPath, testPath);
		
		
		
	}

}
