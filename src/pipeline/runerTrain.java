/**
 * pipeline for training
 * loading in the data; extracting features; training; output model file
 * */

package pipeline;

import java.io.File;

import wd.i2b2.utilities.Document;

public class runerTrain {

	
	public static void main(String[] args) throws Exception {
		String dirPath = args[0];
		System.out.println("Processing the folder:\t" + dirPath);
		File dir=new File(dirPath);
		String[] filenameList=dir.list();
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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("XMLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
					e.printStackTrace();
				}
        	}
        }

	}

}
