package Misc;

import java.util.Arrays;

public class Cloudant {
    public static void main(String[] args) {
        Predictor predictor = new Predictor(50, 1, 45, "MLR_Single_File");
        predictor.predict(true);
        double[] predictedVariables = predictor.getPredictedVariables();
        System.out.println(Arrays.toString(predictedVariables));
    }
}

