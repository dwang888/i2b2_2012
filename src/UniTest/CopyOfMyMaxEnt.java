package UniTest;

import cc.mallet.classify.*;
import java.io.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.*;

import cc.mallet.types.*;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.iterator.SimpleFileLineIterator;

import cc.mallet.util.CommandOption;
import cc.mallet.util.MalletLogger;

public class CopyOfMyMaxEnt {

	private static Logger logger = MalletLogger
			.getLogger(BinaryFeatureMaxEnt.class.getName());

	private CopyOfMyMaxEnt() {
	}

	private static final CommandOption.Double gaussianVarianceOption = new CommandOption.Double(
			BinaryFeatureMaxEnt.class, "gaussian-variance", "DECIMAL", true,
			10.0, "The gaussian prior variance used for training.", null);

	private static final CommandOption.File trainOption = new CommandOption.File(
			BinaryFeatureMaxEnt.class, "train", "FILENAME", true, null,
			"Training file", null);

	private static final CommandOption.File testOption = new CommandOption.File(
			BinaryFeatureMaxEnt.class, "test", "FILENAME", true, null,
			"Test file", null);

	private static final CommandOption.Boolean scoreOption = new CommandOption.Boolean(
			BinaryFeatureMaxEnt.class, "score", "true|false", true, false,
			"Whether to measure labeling accuracy", null);

	private static final CommandOption.File modelOption = new CommandOption.File(
			BinaryFeatureMaxEnt.class,
			"model-file",
			"FILENAME",
			true,
			null,
			"The filename for reading (train/run) or saving (train) the model.",
			null);

	private static final CommandOption.Integer iterationsOption = new CommandOption.Integer(
			BinaryFeatureMaxEnt.class, "iterations", "INTEGER", true, 500,
			"Number of training iterations", null);

	private static final CommandOption.Integer featureSelectionOption = new CommandOption.Integer(
			BinaryFeatureMaxEnt.class, "feature-selection", "INTEGER", true, 0,
			"Number of feature to select.", null);

	private static final CommandOption.Boolean posteriorOutputOption = new CommandOption.Boolean(
			BinaryFeatureMaxEnt.class, "posterior-output", "true|false", true,
			false, "Whether to output posterior probabilities instead", null);

	private static final CommandOption.Boolean parameterOutputOption = new CommandOption.Boolean(
			BinaryFeatureMaxEnt.class, "print-parameters", "true|false", true,
			false, "Whether to print model parameters", null);

	private static final CommandOption.Boolean continuousOption = new CommandOption.Boolean(
			BinaryFeatureMaxEnt.class, "continuous", "true|false", true,
			false, "Whether to use continuous features", null);

	private static final CommandOption.Double l1WeightOption = new CommandOption.Double(
			BinaryFeatureMaxEnt.class, "l1-weight", "DECIMAL", true,
			0.0, "The L1 prior used for training.", null);

	private static final CommandOption.List commandOptions = new CommandOption.List(
			"Training, testing and running a binary feature MaxEnt classifier.",
			new CommandOption[] { gaussianVarianceOption, trainOption,
					iterationsOption, testOption, modelOption, scoreOption,
					featureSelectionOption, posteriorOutputOption,
					parameterOutputOption, continuousOption, l1WeightOption });

	public static void main(String[] args) throws Exception {
		File trainingFile = null, testFile = null;
		InstanceList trainingData = null, testData = null;
		int restArgs = commandOptions.processOptions(args);
		if (trainOption.value != null) {
			trainingFile = trainOption.value;
		}
		if (testOption.value != null) {
			testFile = testOption.value;
		}

		Pipe p = null;
		Classifier c = null;
		if (trainOption.value == null) {
			if (modelOption.value == null) {
				commandOptions.printUsage(true);
				throw new IllegalArgumentException("Missing model file option");
			}
			ObjectInputStream s = new ObjectInputStream(new FileInputStream(
					modelOption.value));
			c = (Classifier) s.readObject();
			s.close();
			p = c.getInstancePipe();
		} else {
			ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
			if (continuousOption.value) {
				pipeList.add(new Lines2FeatureVectors(Lines2FeatureVectors.FeatureType.CONTINUOUS));
			} else {
				pipeList.add(new Lines2FeatureVectors(Lines2FeatureVectors.FeatureType.BINARY));
			}
			p = new SerialPipes(pipeList);
		}

		if (trainOption.value != null) {
			p.setTargetProcessing(true);
			trainingData = new InstanceList(p);
			trainingData.addThruPipe(new SimpleFileLineIterator(trainingFile));
			logger.info("Number of binary features: "
					+ p.getDataAlphabet().size());
		}
		if (testOption.value != null) {
			if (scoreOption.value) {
				p.setTargetProcessing(true);
			} else {
				p.setTargetProcessing(false);
			}
			testData = new InstanceList(p);
			testData.addThruPipe(new SimpleFileLineIterator(testFile));
			logger.info("Number of binary features: "
					+ p.getDataAlphabet().size());
		}

		if (p.isTargetProcessing()) {
			Alphabet targets = p.getTargetAlphabet();
			StringBuffer buf = new StringBuffer("Labels:");
			for (int i = 0; i < targets.size(); i++)
				buf.append(" ").append(targets.lookupObject(i).toString());
			logger.info(buf.toString());
		}

		if (trainOption.value != null) {
			MaxEntTrainer trainer = new MaxEntTrainer(
					gaussianVarianceOption.value);
			if (l1WeightOption.value != 0) {
				trainer.setL1Weight(l1WeightOption.value);
			}
			if (featureSelectionOption.value > 0) {
				FeatureSelectingClassifierTrainer fsTrainer = new FeatureSelectingClassifierTrainer(
						trainer, new FeatureSelector(new InfoGain.Factory(),
								featureSelectionOption.value));
				c = fsTrainer.train(trainingData);
			} else {
				c = trainer.train(trainingData);
			}
			if (modelOption.value != null) {
				ObjectOutputStream s = new ObjectOutputStream(
						new FileOutputStream(modelOption.value));
				s.writeObject(c);
				s.close();
			}
			if (parameterOutputOption.value) {
				print((MaxEnt) c);
			} else if (testData != null) {
				if (scoreOption.value) {
					Trial trial = new Trial(c, testData);
					logger.info("Test accuracy: " + trial.getAccuracy());
				}
				if (posteriorOutputOption.value) {
					for (Instance inst : testData) {
						Labeling labeling = c.classify(inst).getLabeling();
						LabelAlphabet labelAlphabet = labeling
								.getLabelAlphabet();
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i != labelAlphabet.size(); i++) {
							sb.append(labelAlphabet.lookupLabel(i));
							sb.append("=");
							sb.append(String.format("%f",
									Math.log(labeling.value(i))));
							if (i != labelAlphabet.size()) {
								sb.append("\t");
							}
						}
						System.out.println(sb.toString());
					}
				} else {
					for (Instance inst : testData) {
						System.out.println(c.classify(inst).getLabeling()
								.getBestLabel());
					}
				}
			}
		} else {
			if (c == null) {
				if (modelOption.value == null) {
					commandOptions.printUsage(true);
					throw new IllegalArgumentException(
							"Missing model file option");
				}
				ObjectInputStream s = new ObjectInputStream(
						new FileInputStream(modelOption.value));
				c = (Classifier) s.readObject();
				s.close();
			}
			if (parameterOutputOption.value) {
				print((MaxEnt) c);
			} else if (testData != null) {
				if (scoreOption.value) {
					Trial trial = new Trial(c, testData);
					logger.info("Test accuracy: " + trial.getAccuracy());
				}
				if (posteriorOutputOption.value) {
					for (Instance inst : testData) {
						Labeling labeling = c.classify(inst).getLabeling();
						LabelAlphabet labelAlphabet = labeling
								.getLabelAlphabet();
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i != labelAlphabet.size(); i++) {
							sb.append(labelAlphabet.lookupLabel(i));
							sb.append("=");
							sb.append(String.format("%f",
									Math.log(labeling.value(i))));
							if (i != labelAlphabet.size()) {
								sb.append("\t");
							}
						}
						System.out.println(sb.toString());
					}
				} else {
					for (Instance inst : testData) {
						System.out.println(c.classify(inst).getLabeling()
								.getBestLabel());
					}
				}
			}
		}
	}

	static void print(MaxEnt c) {
		final Alphabet dict = c.getAlphabet();
		final LabelAlphabet labelDict = c.getLabelAlphabet();

		int numFeatures = dict.size() + 1;
		int numLabels = labelDict.size();
		double[] parameters = c.getParameters();
		int defaultFeatureIndex = c.getDefaultFeatureIndex();

		// Include the feature weights according to each label
		for (int li = 0; li < numLabels; li++) {
			System.out.println("FEATURES FOR CLASS "
					+ labelDict.lookupObject(li));
			System.out.println(" <default> "
					+ parameters[li * numFeatures + defaultFeatureIndex]);
			final double weights[] = Arrays.copyOfRange(parameters, li
					* numFeatures, li * numFeatures + defaultFeatureIndex);
			Integer[] idx = new Integer[weights.length];
			for (int i = 0; i != idx.length; ++i) {
				idx[i] = i;
			}
			Arrays.sort(idx, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					if (weights[(Integer) o1] > weights[(Integer) o2])
						return -1;
					else if (weights[(Integer) o2] > weights[(Integer) o1])
						return 1;
					else
						return 0;
				}
			});
			for (int i : idx) {
				Object name = dict.lookupObject(i);
				double weight = weights[i];
				System.out.println(" " + name + " " + weight);
			}
		}
	}
}
