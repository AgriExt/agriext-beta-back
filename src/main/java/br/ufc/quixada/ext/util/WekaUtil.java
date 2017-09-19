package br.ufc.quixada.ext.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.M5P;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

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

	public static String jensenHaysen(double rad_solar_total, double temp_ar_media) {

		double rad = rad_solar_total / 2450;

		double et0 = rad * (0.025 * temp_ar_media + 0.078);

		return "" + et0;
	}

	public static String quixadaHC(String p) throws Exception {
		String path = "/home/antoniorrm/" + p + ".csv";
		String model = "/home/antoniorrm/m5p-new.model";
		Classifier m5pModel = null;
		try {
			m5pModel = (Classifier) weka.core.SerializationHelper.read(new FileInputStream(model));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Instances test = csv2arff(path).getInstances();
		int index = test.numAttributes() - 1;
		
		if (test.classIndex() == -1)
			test.setClassIndex(index);

		String resultado = "";
		for (Instance instance : test) {
			double label = m5pModel.classifyInstance(instance);
			instance.setClassValue(label);
			resultado = resultado + "\n" + instance.value(index);
		}

		return resultado;

	}

	public static String classifie(int type, String name) {

		double percent = 70;
		int seed = 1;
		Random rnd = new Random(seed);
		String output;
		String pathOut = "/home/antoniorrm/" + name;
		String path = "/home/antoniorrm/" + name;

		Classifier classify = null;
		if (type == 0) {
			classify = new M5P();
		} else if (type == 1) {
			classify = new LinearRegression();
		}

		Instances inst = csv2arff(path).getInstances();
		inst.setClassIndex(inst.numAttributes() - 1);
		inst.randomize(rnd);

		int trainSize = (int) Math.round(inst.numInstances() * percent / 100);
		int testSize = inst.numInstances() - trainSize;

		Instances train = new Instances(inst, 0, trainSize);
		Instances test = new Instances(inst, trainSize, testSize);

		Evaluation eval = null;
		try {
			classify.buildClassifier(train);
			eval = new Evaluation(train);
			eval.evaluateModel(classify, test);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectOutputStream save;
		try {
			save = new ObjectOutputStream(new FileOutputStream(pathOut + ".model"));
			save.writeObject(classify);
			save.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		output = eval.toSummaryString() + "\nsmashline\n" + classify.toString();
		return output;

	}

	public static void main(String[] args) throws Exception {
		// String output;
		// Classifier classifie = new M5P();
		// Instances data =
		// csv2arff("/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv").getInstances();
		//
		// //Instances data2 =
		// csv2arff("/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv").getInstances();
		// data.setClassIndex(data.numAttributes() - 1);
		// classifie.buildClassifier(data);
		//
		// Evaluation evaluation = new Evaluation(data);
		// for (Instance instance : data) {
		// evaluation.evaluateModelOnceAndRecordPrediction(classifie, instance);
		// }
		//
		// output = evaluation.toSummaryString() + "\n" + classifie.toString();
		//// System.out.println(output);
		// System.out.println(classifie(0, "2016total-semoutlier2.csv"));
		// System.out.println(classifie(1,
		// "/home/antoniorrm/Dropbox/UFC/Bolsa/dataset/Limpos/2016total-semoutlier2.csv"));
		System.out.println(quixadaHC("dataset-test"));
		//

	}
}