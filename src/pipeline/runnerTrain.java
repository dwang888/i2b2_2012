/**
 * pipeline for training
 * loading in the data; extracting features; training; output model file
 * */

package pipeline;

import java.io.File;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;

import wd.i2b2.utilities.Document;
import wd.i2b2.utilities.Tlink;

public class runnerTrain {

	
	public static void main(String[] args) throws Exception {
		String dirPath = args[0];
		System.out.println("Processing the folder:\t" + dirPath);
		File dir=new File(dirPath);
		String[] filenameList=dir.list();
		ClassifierTrainer trainer = new MaxEntTrainer();
		
		
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
					Document doc = new Document(xmlPath, geniaPath, depPath);
					FeatureExtractor fe = new FeatureExtractor(doc);
					System.out.println("training...");
					Classifier classifier = trainer.train(doc.getInsTlink());
//					for(int j = 0; j < doc.getDataSamplesTlink().size(); j++){
//						System.out.println(doc.getDataSamplesTlink().get(j).getFeatures().toString());
//						System.out.println(doc.getDataSamplesTlink());
//					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					System.out.println("XMLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
//	        		System.out.println(xmlPath);
					e.printStackTrace();
				}
        	}
        }

	}

}
