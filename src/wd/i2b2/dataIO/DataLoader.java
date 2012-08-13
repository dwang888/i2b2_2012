package wd.i2b2.dataIO;

import java.io.IOException;

import wd.i2b2.utilities.Configuration;
import wd.i2b2.utilities.DocumentI2b2;



public class DataLoader {
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		DataLoader dl = new DataLoader();
		Configuration cf = new Configuration("etc\\train.properties");
		System.out.println(cf.getValue("corpusTrainXml"));
		System.out.println(cf.getValue("corpusTrainGenia"));
		System.out.println(cf.getValue("corpusTrainDep"));
		
		String xmlPath = "D:\\projects\\i2b2_2012\\data\\2012-06-18.release-fix\\1.xml";
		String geniaPath = "D:\\projects\\i2b2_2012\\data\\2012-06-18_genia\\1.xml.txt.genia";
		String depPath = "D:\\projects\\i2b2_2012\\data\\2012-06-18_depfix\\1.xml.parse";

		DocumentI2b2 doc = new DocumentI2b2(xmlPath, geniaPath, depPath);
//		System.out.println("-----------------");
//		System.out.println(doc.getText());
//		for(int i =0; i < doc.getSentences().size(); i++){
//			System.out.println(doc.getSentences().get(i));
//			for(int j = 0; j < doc.getSentences().get(i).getNumTokens(); j++){
//				System.out.println(doc.getSentences().get(i).getTokens().get(j).getText());
//			}
//		}
	}

}
