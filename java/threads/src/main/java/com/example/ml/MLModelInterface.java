package com.example.ml;

public interface MLModelInterface<T, R> {
    R predict(T input);
    void loadModel(String modelPath);
    double getConfidence();
}