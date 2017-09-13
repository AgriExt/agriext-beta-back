package br.ufc.quixada.ext;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.M5P;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class WekaUtil {

	/**
	 * takes 2 arguments: - CSV input file - ARFF output file
	 */
	public static ArffSaver csv2arff(String path) {
		CSVLoader loader = new CSVLoader();
		Instances data = null;
		try {
			loader.setSource(new File(path));
		
			data = loader.getDataSet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// save ARFF
		ArffSaver saver = new ArffSaver();
		if (data != null) {
			saver.setInstances(data);
			return saver;
		}
		return saver;

	}

	public static String classifie(int type, String path) throws Exception {
		
		double percent = 70; 
		int seed=1;
		Random rnd = new Random(seed);
		String output;

		Classifier classify = null;
		if (type == 0) {
			classify = new M5P();
		}else if (type == 1) {
			classify = new LinearRegression();
		}
		
		Instances inst = csv2arff(path).getInstances();
		inst.setClassIndex(inst.numAttributes() - 1);
		inst.randomize(rnd);
		
		

		int trainSize = (int) Math.round(inst.numInstances() * percent/100);
		int testSize = inst.numInstances() - trainSize;

		Instances train = new Instances(inst, 0, trainSize);
		Instances test = new Instances(inst, trainSize, testSize);

		classify.buildClassifier(train);

		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(classify, test);
		
		output = eval.toSummaryString() + "\n" + classify.toString();
		return output;

	}

	public static void main(String[] args) throws Exception {
//		String output;
//		Classifier classifie = new M5P();
//		Instances data = csv2arff("/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv").getInstances();
//		
//		//Instances data2 = csv2arff("/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv").getInstances();
//		data.setClassIndex(data.numAttributes() - 1);
//		classifie.buildClassifier(data);
//
//		Evaluation evaluation = new Evaluation(data);
//		for (Instance instance : data) {
//			evaluation.evaluateModelOnceAndRecordPrediction(classifie, instance);
//		}
//		
//		output = evaluation.toSummaryString() + "\n" + classifie.toString();
////		System.out.println(output);
		System.out.println(classifie(0, "/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv"));
		System.out.println(classifie(1, "/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv"));
//

	}
} // End of the class //