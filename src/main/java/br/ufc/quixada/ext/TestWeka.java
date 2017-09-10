package br.ufc.quixada.ext;

import weka.classifiers.Classifier;
import weka.classifiers.trees.M5P;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import static org.mockito.Matchers.startsWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

	public static String classifie() throws Exception {
		Classifier classifie = new M5P();
		Instances data = csv2arff("/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv").getInstances();
		data.setClassIndex(data.numAttributes() - 1);
		classifie.buildClassifier(data);
		return classifie.toString();

	}

//	public static void main(String[] args) throws Exception {
//		Classifier classifie = new M5P();
//		Instances data = csv2arff("/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv").getInstances();
//		data.setClassIndex(data.numAttributes() - 1);
//		classifie.buildClassifier(data);
//
//
//		System.out.println(classifie.toString());
//
//
//	}
} // End of the class //