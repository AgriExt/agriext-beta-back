package br.ufc.quixada.ext.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	
	 /*
	 * COLOQUE O CAMINHO DO SEU PROJETO 
	 */
	static String myPath = "/home/antoniorrm/";
		

	/**
	 * CONVERTE O CSV EM UM ARFF
	 */
	public static ArffSaver csv2arff(String path) {
		CSVLoader loader = new CSVLoader();
		Instances data = null;
		try {
			loader.setSource(new File(path));
			data = loader.getDataSet();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Salva ARFF
		ArffSaver saver = new ArffSaver();
		if (data != null) {
			saver.setInstances(data);
			return saver;
		}
		return saver;

	}

	//Gera o ET0 a partir de um Modelo enviado
	public static String modelEt0(double kc, String nome, String modelNome) throws Exception {
		String path = myPath + nome;
		String model = myPath + modelNome;
		Classifier Model = null;
		try {
			Model = (Classifier) weka.core.SerializationHelper.read(new FileInputStream(model));
		} catch (FileNotFoundException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} catch (ClassNotFoundException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}

		Instances test = csv2arff(path).getInstances();
		int index = test.numAttributes() - 1;
		
		if (test.classIndex() == -1)
			test.setClassIndex(index);

		String resultado = "";
		double evapo = 0.0;
		
		for (Instance instance : test) {
			double label = Model.classifyInstance(instance);
			instance.setClassValue(label);
			double value = instance.value(index);
			double kcValue = value * kc;
			resultado = resultado + value + ", " + kcValue + "\n";
			evapo = evapo +value;
		}
		double irriga = evapo * kc;
	
		resultado = resultado + evapo +", " + irriga;
		return extracted(resultado);
	}

	/*
	 * Gera o ET0 a partir de um Modelo m5p-new.model
	 * Modelo criado por Hinessa Caminha - UFC Quixadá
	 */
	public static String quixadaHC(double kc, String nome) throws Exception {
		String path = myPath + nome;
		
		//Caminho para o Modelo m5p-new.model
		String model = "/home/antoniorrm/Dropbox/UFC/Bolsa/demoback/m5p-new.model";
		
		Classifier m5pModel = null;
		try {
			m5pModel = (Classifier) weka.core.SerializationHelper.read(new FileInputStream(model));
		} catch (FileNotFoundException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} catch (ClassNotFoundException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}

		Instances test = csv2arff(path).getInstances();
		int index = test.numAttributes() - 1;
		
		if (test.classIndex() == -1)
			test.setClassIndex(index);

		String resultado = "";
		double evapo = 0.0;
		
		for (Instance instance : test) {
			double label = m5pModel.classifyInstance(instance);
			instance.setClassValue(label);
			double value = instance.value(index);
			double kcValue = value * kc;
			resultado = resultado + value + ", " + kcValue + "\n";
			evapo = evapo +value;
		}
		double irriga = evapo * kc;
	
		resultado = resultado + evapo +", " + irriga;
		return extracted(resultado);

	}

	@SuppressWarnings("resource")
	public static String classifier(int type, String name) {

		double percent = 70;
		int seed = 1;
		Random rnd = new Random(seed);
		String output = "";
		String pathOut = myPath + name;
		String path = myPath + name;

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
			return e.getMessage();
		}

		ObjectOutputStream save;
		try {
			save = new ObjectOutputStream(new FileOutputStream(pathOut + ".model"));
			save.writeObject(classify);
			save.flush();
		} catch (IOException e) {
			return e.getMessage();
		}

		output = eval.toSummaryString() + "\nsmashline\n" + classify.toString();
		return extracted(output);

	}

	
	private static String extracted(String output) {
		return output;
	}
}