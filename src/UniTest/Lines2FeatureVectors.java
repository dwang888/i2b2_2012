package UniTest;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.*;

public class Lines2FeatureVectors extends Pipe {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7689097595549652547L;

	public enum FeatureType { BINARY, CONTINUOUS };
	
	FeatureType featureType;
	
	public Lines2FeatureVectors(FeatureType type) {
		super(new Alphabet(), new LabelAlphabet());
		featureType = type;
	}

	private String[] parseLine(String line) {
		if (line.isEmpty())
			return new String[0];
		String[] tokens = line.split(" ");
		return tokens;
	}
	
	private Object[] parseToken(String token) {
		Object out[] = new Object[2];
		String[] fields = token.split("=");
		out[0] = fields[0];
		out[1] = Double.parseDouble(fields[1]);
		return out;
	}

	public Instance pipe(Instance carrier) {
		Object inputData = carrier.getData();
		Alphabet features = getAlphabet();
		LabelAlphabet labels = (LabelAlphabet) getTargetAlphabet();
		String target = null;
		String[] tokens;
		double[] values = null;
		int featureIndices[] = null;
		tokens = parseLine((String) inputData);
		if (isTargetProcessing()) {
			target = tokens[0];
			values = new double[tokens.length - 1];
			featureIndices = new int[tokens.length - 1];
			for (int i = 1; i != tokens.length; i++) {
				if (featureType == FeatureType.BINARY) {
					featureIndices[i - 1] = features.lookupIndex(tokens[i]);
				} else {
					Object[] fields = parseToken(tokens[i]);
					featureIndices[i - 1] = features.lookupIndex(fields[0]);
					values[i - 1] = (Double) fields[1];
				}
			}
		} else {
			values = new double[tokens.length];
			featureIndices = new int[tokens.length];
			for (int i = 0; i != tokens.length; i++) {
				if (featureType == FeatureType.BINARY) {
					featureIndices[i] = features.lookupIndex(tokens[i]);
				} else {
					Object[] fields = parseToken(tokens[i]);
					featureIndices[i] = features.lookupIndex(fields[0]);
					values[i] = (Double) fields[1];
				}
			}
		}
		if (featureType == FeatureType.BINARY) {
			carrier.setData(new FeatureVector(features, featureIndices));
		} else {
			carrier.setData(new FeatureVector(features, featureIndices, values));
		}
		if (isTargetProcessing())
			carrier.setTarget(labels.lookupLabel(target));
		else
			carrier.setTarget(labels.lookupLabel(0));
		return carrier;
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}
