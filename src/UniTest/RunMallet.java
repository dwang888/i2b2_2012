package UniTest;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.classify.evaluate.ConfusionMatrix;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelVector;
import cc.mallet.types.Labeling;
import cc.mallet.types.MatrixOps;

public class RunMallet {
	public static void main(String[] args) throws Exception {
		
		// create the command line parser
		CommandLineParser parser = new PosixParser();
		// create the Options
		Options options = new Options();
		options.addOption( "t", "test", true, "testing path" );
		options.addOption( "r", "train", true, "training path" );
		options.addOption( "f", "feature", true, "feature file" );
		options.addOption( "e", "evaluate", false, "evaluate ouput against gold standard data from input if available");
		options.addOption( "o", "output", true, "output data" );
		options.addOption( "c", "classifier", true, "path to classifier" );
		options.addOption( "h", "help", false, "usage" );
		options.addOption( "k", "gazetteerinput", true, "create gazetteer file from *.a2 files" );
		options.addOption( "g", "gazetteer", true, "path to gazetteer file- give filename" );options.addOption( "g", "gazetteer", true, "path to gazetteer file" );
		options.addOption( "n", "trainthreshold", true, "minimum number of feature to use feature - training" );
		options.addOption( "m", "testthreshold", true, "minimum number of feature to use feature - testing" );
		String trainPath=null;
		String testPath=null;
		String outputPath=null;
		String classifierPath=null;
		String featurePath=null;
		String[] trainGazetteerPath=null;
		String gazetteerPath=null;
		int trainThreshold=0;
		int testThreshold = 0;
		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );

		    // validate that block-size has been set
		    if( line.hasOption( "help" ) ) {
		    	HelpFormatter formatter = new HelpFormatter();
		    	formatter.printHelp( "x", options );
		    	System.exit(0);
		    }
		    if( line.hasOption( "output" )  ) {
		    	outputPath = line.getOptionValue( "output" );
		    }
		    if( line.hasOption( "classifier" )  ) {
		    	classifierPath = line.getOptionValue( "classifier" );
		    }
		    if( line.hasOption( "feature" )  ) {
		    	featurePath = line.getOptionValue( "feature" );
		    }
		    if( line.hasOption( "test" )  ) {
		    	testPath = line.getOptionValue( "test" );
		    }
		    if( line.hasOption( "train" )  ) {
		    	trainPath = line.getOptionValue( "train" );
		    }
		    if( line.hasOption( "gazetteer" )  ) {
		    	gazetteerPath = line.getOptionValue( "gazetteer" );
		    }
		    if( line.hasOption( "gazetteerinput" )  ) {
		    	trainGazetteerPath = line.getOptionValues("gazetteerinput");
		    }
		    if( line.hasOption( "trainthreshold" )  ) {
		    	trainThreshold = Integer.parseInt(line.getOptionValue("trainthreshold"));
		    }
		    if( line.hasOption( "testthreshold" )  ) {
		    	testThreshold = Integer.parseInt(line.getOptionValue("testthreshold"));
		    }
		}
		catch( ParseException exp ) { System.out.println( "Unexpected exception:" + exp.getMessage() ); }

		// 2 Workflows
		// 1. Train Gazetteer
		// 2. Train and test classifier
		
		Classifier classifier = null;
		if(trainGazetteerPath!=null){
			// To train gazetteer, use -k command line option, for example
			// -k /home/nico/Projects/bionlp/data/system/training-system
			// multiple files can be given but they each need to have -k option preceding ie -k file1 -k file2 -k file3
			Corpus c = new Corpus();
			for(String path:trainGazetteerPath){
				System.out.println("Gazetteer path:"+path);
				c.loadAllFiles(path);
			}
			System.out.println("writing gazetteer...");
			Gazetteer g = new Gazetteer(c);
			g.write(gazetteerPath);
			System.out.println("Gazetteer created:"+gazetteerPath);
		}
		else if(testPath!=null){
			trainThreshold = 2; // minimum occurrence of a feature is > 2
			testThreshold = 0;
			
			// load gazetteer
			Gazetteer g = new Gazetteer(gazetteerPath);
			
			File train = createVector(trainPath, outputPath, featurePath, g, "train.vectors", trainThreshold);
			File test = createVector(testPath,  outputPath, featurePath, g, "test.vectors", testThreshold); // no threshold for test
			
			//File test
			File trainV = new File(outputPath+"train.vectors");
			File testV = new File(outputPath+"test.vectors");
			InstanceList[] x0 = info2Vectors(train, trainV, null);
			InstanceList[] x1 = info2Vectors(test, testV, trainV);
			InstanceList testList = x1[0];
			InstanceList trainList = x1[1];
			classifier = trainClassifier(trainList);
			classifyData(testList,classifier,outputPath);
			System.out.println();
			System.out.println("trainThreshold="+trainThreshold);
			System.out.println("testThreshold="+testThreshold);
			Feature.printFile(featurePath);
			
		}

		
		
        System.exit(0);
	}
	
	private static InstanceList[] info2Vectors(File inputFile, File outputFile, File usePipeFromVectorsFile) throws Exception {
		// Step 2: get the pipe
		Pipe instancePipe;
		InstanceList previousInstanceList = null;
		
		if (usePipeFromVectorsFile==null) {
		    instancePipe = new SerialPipes (new Pipe[] {
			new Target2Label (),
			(Pipe) new CharSequence2TokenSequence(CharSequenceLexer.LEX_NONWHITESPACE_TOGETHER),

			// 572: replace two pipes		
			(Pipe) new TokenSequence2FeatValueSequence(),
			(Pipe) new FeatValueSequence2AugmentableFeatureVector(), 
			// or FeatureSequence2FeatureVector
			//new PrintInputAndTarget ()
		    });
		}
		else {
		    previousInstanceList = InstanceList.load (usePipeFromVectorsFile);
		    instancePipe = previousInstanceList.getPipe();			
		}
		InstanceList ilist = new InstanceList (instancePipe);
		FileReader fileReader = new FileReader (inputFile);
		ilist.addThruPipe( new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
					   3, 2, 1));

		ObjectOutputStream oos;
		if (outputFile.toString().equals ("-"))
		    oos = new ObjectOutputStream(System.out);
		else
		    oos = new ObjectOutputStream(new FileOutputStream(outputFile));
		oos.writeObject(ilist);
		oos.close();	 	
		// *rewrite* vector file used as source of pipe in case we changed the alphabet(!)
		
		if (usePipeFromVectorsFile!=null){
		    System.out.println(" output usepipe ilist pipe instance id =" + previousInstanceList.getPipe().getInstanceId());
		    oos = new ObjectOutputStream(new FileOutputStream(usePipeFromVectorsFile));
		    oos.writeObject(previousInstanceList);
		    oos.close();
		    
		    InstanceList[] x = {ilist,previousInstanceList};
		    return x;
		}
		
		InstanceList[] x = {ilist};
		return x;
	}
	
	private static void classifyData(InstanceList test, Classifier classifier, String outputPath) throws Exception {
		Trial testTrial = new Trial (classifier, test);
		ConfusionMatrix x = new ConfusionMatrix (testTrial);
		System.out.println("TESTING");
		System.out.println(x.toString());
		printPerClassAccuracy(testTrial);
		
		File fClass = new File(outputPath+"trigger.classification");
		fClass.createNewFile();
		BufferedWriter classFile = new BufferedWriter(new FileWriter(fClass));
		Iterator<Classification> iterator = testTrial.iterator();
		while (iterator.hasNext()) {
			
			// Get classification info for a particular entity
			Classification c = iterator.next();
			String name = c.getInstance().getName().toString();
			Labeling l = c.getLabeling();
			
			String[] nameArr = name.split("_"); // form is DOC_SENTENCE_TOKEN easy cheezy			
			for(String s: nameArr)
				classFile.write(s+"\t");
			
			int numClasses = l.numLocations();
			for(int i=0;i<numClasses;i++){
				String cls = l.getLabelAtRank(i).toString();
				double val = l.getValueAtRank(i);
				classFile.write(cls+"\t"+val+"\t");
			}
			classFile.write("\n");
			
		}
		classFile.close();
	}
	
	/**
	 * Train classifier on instance list
	 * @param train training data
	 * @return classifier
	 * @throws Exception
	 */
	private static Classifier trainClassifier(InstanceList train) throws Exception {
		ClassifierTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(train);
		Trial trainTrial = new Trial (classifier, train);

		ConfusionMatrix x = new ConfusionMatrix (trainTrial);
		System.out.println("TRAINING");
		System.out.println(x.toString());
		printPerClassAccuracy(trainTrial);
		
		return classifier;
	}
	
	/**
	 * Createa Mallet input vector file
	 * @param inputPath
	 * @param outputPath
	 * @param featurePath
	 * @param g
	 * @param vector
	 * @param threshold
	 * @return
	 * @throws Exception
	 */
	private static File createVector(String inputPath, String outputPath, String featurePath, Gazetteer g, String vector, int threshold) throws Exception{
		if( inputPath.equals(null) )
			throw new Exception("Empty input path");	
		if( outputPath.equals(null) )
			throw new Exception("Empty output path");
		
		// Read input data
		Corpus corpus = new Corpus();
		corpus.loadAllFiles(inputPath); // requires .dep, .morph, .a1, .a2
		System.out.println("Loading features");
		ArrayList<Feature> features = Feature.parseConfigFile(featurePath);
		
		System.out.println("Generating features");
		ArrayList<classifyTriggers.Instance> featureForToken = Feature.generateForToken(corpus, features, g, threshold);
		
		// print out vector
		String vectorFile = outputPath+vector+".txt";
		File fVec = new File(vectorFile);
		System.out.println("Printing vector file: "+fVec.toString());
		Feature.printVector(featureForToken,fVec);
		return fVec;
	}
	
	/**
	 * Print out accuracy per class (and micro/macro) for a trial
	 * @param t trial
	 */
	private static void printPerClassAccuracy(Trial t){
		ArrayList classifications;
		int[][] values;
		classifications = t;
		Labeling tempLabeling =	((Classification)classifications.get(0)).getLabeling();
		int numClasses = tempLabeling.getLabelAlphabet().size();
		values = new int[numClasses][numClasses];
		LabelAlphabet labelAlphabet = t.getClassifier().getLabelAlphabet();
		
		// Get the numbers of classifications per label
		for(int i=0; i < classifications.size(); i++)
		{
			LabelVector lv =
				((Classification)classifications.get(i)).getLabelVector();
			Instance inst = ((Classification)classifications.get(i)).getInstance();
			int bestIndex = lv.getBestIndex();
			int correctIndex = inst.getLabeling().getBestIndex();
			assert(correctIndex != -1);
			//System.out.println("Best index="+bestIndex+". Correct="+correctIndex);
			values[correctIndex][bestIndex]++;
		}
		
		// Get the max label length so we can nicely format the confusion matrix
		int maxLabelNameLength = 0;
		for (int i = 0; i < numClasses; i++) {
			int len = labelAlphabet.lookupLabel(i).toString().length();
			if (maxLabelNameLength < len)
				maxLabelNameLength = len;
		}
		
		// calculate p/r per class and micro/macro
		double pMacro = 0;
		double rMacro = 0;
		double sumTP = 0;
		double sumFP = 0;
		double sumFN = 0;
		
		// get per class counts
		for (int c = 0; c < numClasses; c++) {
			String labelName = labelAlphabet.lookupLabel(c).toString();
			int total = MatrixOps.sum(values[c]);
			
			HashMap<Integer,Integer> map = new HashMap();
			for (int c2 = 0; c2 < numClasses; c2++) {
				map.put(c2, values[c][c2]);
			}
			
			double precision = 0;
			double recall = 0;
			
			int tp = map.get(c);
			int fp = -tp;
			for (int c2 = 0; c2 < numClasses; c2++)
				fp += values[c2][c];
			int fn = -tp;
			for (int c2 = 0; c2 < numClasses; c2++)
				fn += values[c][c2];
			
			if(tp+fp>0)
				precision = (double)tp/((double)tp+(double)fp);
			if(tp+fn>0)
				recall = (double)tp/((double)tp+(double)fn);
			
			if(!labelName.equals("None")){
				pMacro+=precision;
				rMacro+=recall;
				sumTP+=tp;
				sumFP+=fp;
				sumFN+=fn;
			}
			
			// print the per class accuracy and recall
			System.out.print(labelName);
			int tpfp = tp+fp;
			int tpfn = tp+fn;
			for (int i = 0; i < maxLabelNameLength-labelName.length(); i++) System.out.print(' ');
			System.out.println("\t"+precision+"\t"+recall+"\t"+tp+"/"+tpfp+"\t"+tp+"/"+tpfn);
		}
		
		// print macro and micro
		pMacro/=numClasses;
		rMacro/=numClasses;
		double pMicro = sumTP/(sumTP+sumFP);
		double rMicro = sumTP/(sumTP+sumFN);
		System.out.println("Macro"+"\t"+pMacro+"\t"+rMacro);
		System.out.println("Micro"+"\t"+pMicro+"\t"+rMicro);
	}
}
