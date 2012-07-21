package wd.i2b2.dataIO;

import wd.i2b2.utilities.Configuration;



public class DataLoader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataLoader dl = new DataLoader();
		Configuration cf = new Configuration("etc\\train.properties");
		System.out.println(cf.getValue("corpusTestWsjClosed"));
	}

}
