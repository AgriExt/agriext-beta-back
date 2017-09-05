package br.ufc.quixada.ext;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.M5P;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TestWeka {

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

	public static void main(String[] args) throws Exception {
		// if (args.length != 2) {
		// System.out.println("\nUsage: CSV2Arff <input.csv> <output.arff>\n");
		// System.exit(1);
		// }

		// load CSV
		Classifier classifie = new M5P();
		Instances data = csv2arff("/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv").getInstances();
		data.setClassIndex(data.numAttributes() - 1);
		classifie.buildClassifier(data);
		
		
		System.out.println(classifie.toString());
	

	}
} // End of the class //