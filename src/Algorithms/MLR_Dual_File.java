/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Regression.MultipleLinearRegression;

import java.text.DecimalFormat;

/**
 * @author Adish
 */
public class MLR_Dual_File {

    double[] temperature;
    double[] humidity;
    double[] light_intensity;
    double[] rainfall;
    double[] atmospheric_pressure;
    double[] wind_direction;
    double[] wind_speed;

    double[] temperature2;
    double[] humidity2;
    double[] light_intensity2;
    double[] rainfall2;
    double[] atmospheric_pressure2;
    double[] wind_direction2;
    double[] wind_speed2;
    int ptime;
    int window;

    public MLR_Dual_File(double[] temperature, double[] humidity, double[] light_intensity, double[] rainfall, double[] pressure, double[] wind_direction, double[] wind_speed, double[] temperature2, double[] humidity2, double[] light_intensity2, double[] rainfall2, double[] pressure2, double[] wind_direction2, double[] wind_speed2) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.light_intensity = light_intensity;
        this.rainfall = rainfall;
        this.atmospheric_pressure = pressure;
        this.wind_direction = wind_direction;
        this.wind_speed = wind_speed;

        this.temperature2 = temperature2;
        this.humidity2 = humidity2;
        this.light_intensity2 = light_intensity2;
        this.rainfall2 = rainfall2;
        this.atmospheric_pressure2 = pressure2;
        this.wind_direction2 = wind_direction2;
        this.wind_speed2 = wind_speed2;

    }

    public double[] predict(int ptime, int window) {
        this.ptime = ptime;
        this.window = window;
        double[] predictionarray = new double[7];
        int sampmin = 1;
        window = window * sampmin;
        DecimalFormat df = new DecimalFormat("#.#");


        for (int k = 0; k < 7; k++) {
            double[] Y, X, Z, X22, Z22, time;
            Y = new double[window];
            X = new double[window];
            Z = new double[window];
            X22 = new double[window];
            Z22 = new double[window];

            double[][] X2 = new double[window][5];
            double[] Y2 = new double[window];
            time = new double[2 * window];
            switch (k) {
                case (0):
                    for (int i = 0; i < window - 1; i++) {
                        Y[i] = temperature[(temperature.length - window) + i];
                        X[i] = temperature[(temperature.length - window - 1) + i];
                        Z[i] = humidity[(temperature.length - window) + i];
                        X22[i] = temperature2[(temperature.length - window) + i];
                        Z22[i] = humidity2[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        X2[i][3] = X22[i];
                        X2[i][4] = Z22[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (1):
                    for (int i = 0; i < window - 1; i++) {
                        Y[i] = humidity[(temperature.length - window) + i];
                        X[i] = humidity[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];
                        X22[i] = humidity2[(temperature.length - window) + i];
                        Z22[i] = temperature2[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        X2[i][3] = X22[i];
                        X2[i][4] = Z22[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (2):
                    for (int i = 0; i < window - 1; i++) {

                        Y[i] = light_intensity[(temperature.length - window) + i];
                        X[i] = light_intensity[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];
                        X22[i] = light_intensity2[(temperature.length - window) + i];
                        Z22[i] = temperature2[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        X2[i][3] = X22[i];
                        X2[i][4] = Z22[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (3):
                    for (int i = 0; i < window - 1; i++) {
                        Y[i] = rainfall[(temperature.length - window) + i];
                        X[i] = rainfall[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];
                        X22[i] = rainfall2[(temperature.length - window) + i];
                        Z22[i] = temperature2[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        X2[i][3] = X22[i];
                        X2[i][4] = Z22[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (4):
                    for (int i = 0; i < window - 1; i++) {
                        Y[i] = atmospheric_pressure[(temperature.length - window) + i];
                        X[i] = atmospheric_pressure[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];
                        X22[i] = atmospheric_pressure2[(temperature.length - window) + i];
                        Z22[i] = temperature2[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        X2[i][3] = X22[i];
                        X2[i][4] = Z22[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (5):
                    for (int i = 0; i < window - 1; i++) {
                        Y[i] = wind_direction[(temperature.length - window) + i];
                        X[i] = wind_direction[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];
                        X22[i] = wind_direction2[(temperature.length - window) + i];
                        Z22[i] = temperature2[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        X2[i][3] = X22[i];
                        X2[i][4] = Z22[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (6):
                    for (int i = 0; i < window - 1; i++) {
                        Y[i] = wind_speed[(temperature.length - window) + i];
                        X[i] = wind_speed[(temperature.length - window - 1) + i];
                        Z[i] = temperature[i];
                        X22[i] = wind_speed2[(temperature.length - window) + i];
                        Z22[i] = temperature2[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        X2[i][3] = X22[i];
                        X2[i][4] = Z22[i];
                        Y2[i] = Y[i];
                    }
                    break;

            }
            for (int n = 0; n <= (2 * window) - 1; n++) {
                time[n] = n;
            }

            double sumX = 0.0, sumZ = 0.0, sumX22 = 0.0, sumZ22 = 0.0, sumY = 0.0, sumt = 0.0, Ymean = 0, Xmean = 0, Zmean = 0, X22mean = 0, Z22mean = 0, tmean = 0;

            for (int n = 0; n <= window - 1; n++) {
                sumX += X[n];
                sumZ += Z[n];
                sumX22 += X22[n];
                sumZ22 += Z22[n];
                sumY += Y[n];
                sumt += time[n];
            }

            Ymean = sumY / window;
            Xmean = sumX / window;
            Zmean = sumZ / window;
            X22mean = sumX22 / window;
            Z22mean = sumZ22 / window;
            tmean = sumt / window;

            double ssx = 0.0, ssy = 0.0, scxy = 0.0, sst = 0.0;
            double sctx = 0.0, sctx22 = 0.0, sctz = 0.0, sctz22 = 0.0, r, r2, pred_s41 = 0, pred_r41 = 0, actual_s41 = 0, actual_t41 = 0;

            for (int n = 0; n <= window - 1; n++) {
                ssx += ((X[n] - Xmean) * (X[n] - Xmean));
                ssy += (Y[n] - Ymean) * (Y[n] - Ymean);
                sst += (time[n] - tmean) * (time[n] - tmean);
                scxy += (X[n] - Xmean) * (Y[n] - Ymean);
                sctx += (X[n] - Xmean) * (time[n] - tmean);
                sctz += (Z[n] - Zmean) * (time[n] - tmean);
                sctx22 += (X22[n] - X22mean) * (time[n] - tmean);
                sctz22 += (Z22[n] - Z22mean) * (time[n] - tmean);

            }

            double betatX1 = sctx / sst;
            double betatX0 = Xmean - (betatX1 * tmean);
            double betatZ1 = sctz / sst;
            double betatZl0 = Zmean - (betatZ1 * tmean);

            double betatX122 = sctx22 / sst;
            double betatX022 = X22mean - (betatX122 * tmean);
            double betatZ122 = sctz22 / sst;
            double betatZl022 = Z22mean - (betatZ122 * tmean);

            int pvalue = ptime * sampmin;

            MultipleLinearRegression regression = new MultipleLinearRegression(X2, Y2);
            double newpred_Hr41 = betatX0 + (betatX1 * time[pvalue]);
            double newpred_Lr41 = betatZl0 + (betatZ1 * time[pvalue]);
            double newpred_Hr4122 = betatX022 + (betatX122 * time[pvalue]);
            double newpred_Lr4122 = betatZl022 + (betatZ122 * time[pvalue]);

            double newbeta0 = regression.beta(0);
            double newbeta1 = regression.beta(1);
            double newbeta2 = regression.beta(2);
            double newbeta3 = regression.beta(3);
            double newbeta4 = regression.beta(4);
            double newpred_T41 = newbeta0 + newbeta1 * newpred_Hr41 + newbeta2 * newpred_Lr41 + newbeta3 * newpred_Hr4122 + newbeta4 * newpred_Lr4122;
            predictionarray[k] = Double.parseDouble(df.format(newpred_T41));
        }

        return predictionarray;

    }

}
