/**
 * pipeline for training
 * loading in the data; extracting features; training; output model file
 * */

package pipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import wd.i2b2.utilities.DocumentI2b2;
import wd.i2b2.utilities.Tlink;

public class RunnerTrain {
	InstanceList insTrain;
	Pipe finalPipe;
	InstanceList ins;
	
	public RunnerTrain(){
		this.buildPipe();
		this.insTrain = new InstanceList(finalPipe);
		this.ins = new InstanceList(finalPipe);
	}

	public List<DocumentI2b2> processFiles(String dirPath){
		System.out.println("Processing the folder:\t" + dirPath);
		File dir=new File(dirPath);
		String[] filenameList=dir.list();
		List<DocumentI2b2> docs = new ArrayList<DocumentI2b2>();
		
		
		for(int i = 0; i < filenameList.length; i++){
        	String xmlPath = dirPath + File.separator + filenameList[i];
        	if(xmlPath.endsWith("xml")){
        		String txtPath = xmlPath + ".txt";
        		String tlinkPath = xmlPath + ".tlink";
        		String extentPath = xmlPath + ".extent";
        		String geniaPath = xmlPath + ".txt.genia";
        		String depPath = xmlPath + ".dep";
//        		System.out.println(xmlPath + "\t"
//        				+ txtPath + "\t" 
//        				+ tlinkPath + "\t" 
//        				+ extentPath + "\t" 
//        				+ geniaPath);
        		try {
					DocumentI2b2 doc = new DocumentI2b2(xmlPath, geniaPath, depPath);
//					System.out.println(doc.getNumSents());
					FeatureExtractor fe = new FeatureExtractor(doc);					
//					for(int j = 0; j < doc.getOrigInstances().size(); j++){
//						System.out.println(doc.getOrigInstances().get(j).getTarget().toString());
////						System.out.println(doc.getOrigInstances());
//					}
					docs.add(doc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					System.out.println("XMLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
	        		System.out.println("===========" + xmlPath);
					e.printStackTrace();
				}
        		
        	}
        }
		
		return docs;
	}
	
	public List<DocumentI2b2> processTestFiles(String dirPath){
		System.out.println("Processing the folder:\t" + dirPath);
		File dir=new File(dirPath);
		String[] filenameList=dir.list();
		List<DocumentI2b2> docs = new ArrayList<DocumentI2b2>();
		
		
		for(int i = 0; i < filenameList.length; i++){
        	String xmlPath = dirPath + File.separator + filenameList[i];
        	if(xmlPath.endsWith("xml")){
        		String txtPath = "";
        		String tlinkPath = "";
        		String extentPath = "";
        		String geniaPath = xmlPath + ".txt.genia";
        		String depPath = "";
//        		System.out.println(xmlPath + "\t"
//        				+ txtPath + "\t" 
//        				+ tlinkPath + "\t" 
//        				+ extentPath + "\t" 
//        				+ geniaPath);
        		try {
					DocumentI2b2 doc = new DocumentI2b2(xmlPath, geniaPath, depPath);
//					System.out.println(doc.getInsTlink().size());
					FeatureExtractor fe = new FeatureExtractor(doc);					
//					for(int j = 0; j < doc.getDataSamplesTlink().size(); j++){
//						System.out.println(doc.getDataSamplesTlink().get(j).getFeatures().toString());
//						System.out.println(doc.getDataSamplesTlink());
//					}
					docs.add(doc);
//					System.out.println();
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					System.out.println("XMLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
	        		System.out.println("===========" + xmlPath);
					e.printStackTrace();
				}
        		
        	}
        }
		
		return docs;
	}
	
	public Pipe buildPipe(){
		ArrayList pipeList = new ArrayList();		
		pipeList.add(new Target2Label());
//		pipeList.add(new SaveDataInSource());//a dummy tlink to create negative training sample
		Pattern tokenPattern = Pattern.compile("[\\w=_]+");
		pipeList.add(new CharSequence2TokenSequence(tokenPattern));
		pipeList.add(new TokenSequence2FeatureSequence());        
		pipeList.add(new FeatureSequence2FeatureVector());
		Pipe finalPipe = new SerialPipes(pipeList);
		this.finalPipe = finalPipe;
		
		return finalPipe;
	}
	
	public Classifier train(List<DocumentI2b2> docsTrain){
		ClassifierTrainer trainer = new MaxEntTrainer();
		int num = 0;
		int numError = 0;
		int numIns = 0;
		
		for(DocumentI2b2 doc : docsTrain){
			num++;
			try {
				numIns += doc.getOrigInstances().size();
//				System.out.println("current doc has training samples:\t" + doc.getOrigInstances().size());
				this.insTrain.addThruPipe(doc.getOrigInstances().iterator());
			} catch (Exception e) {
				numError++;
				e.printStackTrace();
			}
			
		}
		
		System.out.println("total training samples:\t" + numIns);
		Classifier classifier = trainer.train(this.insTrain);
		return classifier;
	}
	
	
	public void decode(Classifier cls, List<DocumentI2b2> docsTest) throws IOException, Exception{
		for(DocumentI2b2 doc : docsTest){
			for(int i = 0; i < doc.getOrigInstances().size(); i++){
				this.ins.addThruPipe(doc.getOrigInstances().get(i));
				Instance in = this.ins.get(this.ins.size()-1);
				Classification result = cls.classify(in);
				String type = result.getLabeling().getBestLabel().toString();
				doc.getDataSamplesTlink().get(i).setType(type);
			}
			doc.output2XML(doc.getXmlPath(), doc.getXmlPath().replace("\\xml", "\\xml_reponse"));
		}
	}
	
	public static void main(String[] args) throws Exception {
		RunnerTrain rt = new RunnerTrain();
		List<DocumentI2b2> docsTrain = rt.processFiles(args[0]);
		
		
		Classifier classifier = rt.train(docsTrain);
		
		List<DocumentI2b2> docsTest = rt.processTestFiles(args[1]);
		
		rt.decode(classifier, docsTest);
		
        

	}

}
