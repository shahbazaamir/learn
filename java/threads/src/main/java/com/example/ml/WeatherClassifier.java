package com.example.ml;

import com.example.weather.WeatherResponse;

/*import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.core.SerializationHelper;


import java.util.ArrayList;

public class WeatherClassifier implements MLModelInterface<WeatherResponse, String> {
    private Classifier model;
    private Instances dataset;
    private double confidence = 0.0;
    
    public WeatherClassifier() {
        setupDataset();
    }
    
    @Override
    public String predict(WeatherResponse weather) {
        if (model == null) return "Model not loaded";
        
        try {
            DenseInstance instance = new DenseInstance(3);
            instance.setDataset(dataset);
            instance.setValue(0, weather.currentWeather.temperature);
            instance.setValue(1, weather.currentWeather.windspeed);
            instance.setValue(2, weather.currentWeather.weathercode);
            
            double result = model.classifyInstance(instance);
            confidence = model.distributionForInstance(instance)[(int)result];
            
            return dataset.classAttribute().value((int)result);
        } catch (Exception e) {
            return "Prediction failed: " + e.getMessage();
        }
    }
    
    @Override
    public void loadModel(String modelPath) {
        try {
            model = (Classifier) SerializationHelper.read(modelPath);
        } catch (Exception e) {
            System.err.println("Failed to load model: " + e.getMessage());
        }
    }
    
    @Override
    public double getConfidence() {
        return confidence;
    }
    
    private void setupDataset() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("temperature"));
        attributes.add(new Attribute("windspeed"));
        attributes.add(new Attribute("weathercode"));
        
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("Pleasant");
        classValues.add("Rainy");
        classValues.add("Windy");
        classValues.add("Cold");
        attributes.add(new Attribute("class", classValues));
        
        dataset = new Instances("weather", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);
    }
}

 */