package br.ufc.quixada.ext;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class TestWeka {

    /*
     * java -cp %WEKA_HOME% 
       weka.classifiers.meta.FilteredClassifier 
       -t ReutersAcq-train.arff 
       -T ReutersAcq-test.arff 
       -W "weka.classifiers.functions.SMO -N 2" 
       -F "weka.filters.unsupervised.attribute.StringToWordVector -S"
     */

    public static void main(final String [] args) throws Exception {
        System.out.println("Running");

        final StringToWordVector filter = new StringToWordVector();
        final Classifier classifier = new SMO(); 

        // Create numeric attributes.
        final String[] keywords = { "test1", "test2"};
        FastVector attributes = new FastVector(keywords.length + 1);
        for (int i = 0 ; i < keywords.length; i++) {
          attributes.addElement(new Attribute(keywords[i]));
        }        
        // Add class attribute.
        final FastVector classValues = new FastVector(2);
        classValues.addElement("miss");
        classValues.addElement("hit");

        attributes.addElement(new Attribute("Class", classValues));

        final Instances data = new Instances("Data1", attributes, 100);
        data.setClassIndex(data.numAttributes() - 1);

        ///////////

        Instance instance = new Instances(10);
        instance.setDataset(data);
        // instance.setValue(testset.attribute(0),testset.attribute(0).addStringValue(obj.toString()));
        System.out.println("==>." + data.attribute(0));
        instance.setValue(data.attribute(0), data.attribute(0).addStringValue("test1"));
        instance.setDataset(data);

        // Add class value to instance.
        instance.setClassValue(1.0);

        // Add instance to training data.
        data.add(instance);

        // Use filter.
        filter.input(instance);
        Instances filteredData = Filter.useFilter(data, filter);

        // Rebuild classifier.
        classifier.buildClassifier(filteredData);
       
    }

} // End of the class //