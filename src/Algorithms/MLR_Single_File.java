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
public class MLR_Single_File {

    double[] temperature;
    double[] humidity;
    double[] light_intensity;
    double[] rainfall;
    double[] atmospheric_pressure;
    double[] wind_direction;
    double[] wind_speed;

    int ptime;
    int window;

    public MLR_Single_File(double[] temperature, double[] humidity, double[] light_intensity, double[] rainfall, double[] pressure, double[] wind_direction, double[] wind_speed) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.light_intensity = light_intensity;
        this.rainfall = rainfall;
        this.atmospheric_pressure = pressure;
        this.wind_direction = wind_direction;
        this.wind_speed = wind_speed;

    }

    public double[] predict(int ptime, int window, int sampling) {
        this.ptime = ptime;
        this.window = window;
        double[] predictionarray = new double[7];
        this.window = window * sampling;
        DecimalFormat df = new DecimalFormat("#.#");


        for (int k = 0; k < 7; k++) {

            double[] Y, X, Z, time;
            Y = new double[window];
            X = new double[window];
            Z = new double[window];

            double[][] X2 = new double[window][3];
            double[] Y2 = new double[window];
            time = new double[2 * window];
            switch (k) {
                case (0)://Temperature
                    for (int i = 0; i < window; i++) {
                        Y[i] = temperature[(temperature.length - window) + i];
                        X[i] = temperature[(temperature.length - window - 1) + i];
                        Z[i] = humidity[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (1):
                    for (int i = 0; i < window; i++) {
                        Y[i] = humidity[(temperature.length - window) + i];
                        X[i] = humidity[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        Y2[i] = Y[i];

                    }
                    break;
                case (2):
                    for (int i = 0; i < window; i++) {
                        Y[i] = light_intensity[(temperature.length - window) + i];
                        X[i] = light_intensity[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        Y2[i] = Y[i];

                    }
                    break;
                case (3):
                    for (int i = 0; i < window; i++) {
                        Y[i] = rainfall[(temperature.length - window) + i];
                        X[i] = rainfall[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (4):
                    for (int i = 0; i < window; i++) {
                        Y[i] = atmospheric_pressure[(temperature.length - window) + i];
                        X[i] = atmospheric_pressure[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (5):
                    for (int i = 0; i < window; i++) {
                        Y[i] = wind_direction[(temperature.length - window) + i];
                        X[i] = wind_direction[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        Y2[i] = Y[i];
                    }
                    break;
                case (6):
                    for (int i = 0; i < window; i++) {
                        Y[i] = wind_speed[(temperature.length - window) + i];
                        X[i] = wind_speed[(temperature.length - window - 1) + i];
                        Z[i] = temperature[(temperature.length - window) + i];

                        X2[i][0] = 1;
                        X2[i][1] = X[i];
                        X2[i][2] = Z[i];
                        Y2[i] = Y[i];
                    }
                    break;

            }
            for (int n = 0; n <= (2 * window) - 1; n++) {
                time[n] = n;
            }

            double sumX = 0.0, sumZ = 0.0, sumY = 0.0, sumt = 0.0, Ymean = 0, Xmean = 0, Zmean = 0, tmean = 0;

            for (int n = 0; n <= window - 1; n++) {
                sumX += X[n];
                sumZ += Z[n];
                sumY += Y[n];
                sumt += time[n];
            }

            Ymean = sumY / window;
            Xmean = sumX / window;
            Zmean = sumZ / window;
            tmean = sumt / window;

            double ssx = 0.0, ssy = 0.0, scxy = 0.0, sst = 0.0;
            double sctx = 0.0, sctz = 0.0, r, r2, pred_s41 = 0, pred_r41 = 0, actual_s41 = 0, actual_t41 = 0;

            for (int n = 0; n <= window - 1; n++) {
                ssx += ((X[n] - Xmean) * (X[n] - Xmean));
                ssy += (Y[n] - Ymean) * (Y[n] - Ymean);
                sst += (time[n] - tmean) * (time[n] - tmean);
                scxy += (X[n] - Xmean) * (Y[n] - Ymean);
                sctx += (X[n] - Xmean) * (time[n] - tmean);
                sctz += (Z[n] - Zmean) * (time[n] - tmean);
            }

            r = scxy / Math.sqrt(ssx * ssy);
            r2 = r * r;
            double beta1 = scxy / ssx;
            double beta0 = Ymean - (beta1 * Xmean);
            double betatX1 = sctx / sst;
            double betatX0 = Xmean - (betatX1 * tmean);
            double betatZ1 = sctz / sst;
            double betatZl0 = Zmean - (betatZ1 * tmean);

            int pvalue = ptime * sampling;

            pred_r41 = betatX0 + (betatX1 * time[pvalue]);
            pred_s41 = beta0 + beta1 * pred_r41;

            double[][] X3 = new double[X2.length][2];
            for (int c = 0; c < 2; c++) {
                for (int c1 = 0; c1 < X2.length; c1++) {
                    X3[c1][c] = X2[c1][c];
                }
            }
            MultipleLinearRegression regression = new MultipleLinearRegression(X2, Y2);

            double newpred_Hr41 = betatX0 + (betatX1 * time[pvalue]);
            double newpred_Lr41 = betatZl0 + (betatZ1 * time[pvalue]);

            double newbeta0 = regression.beta(0);
            double newbeta1 = regression.beta(1);
            double newbeta2 = regression.beta(2);
            double newpred_T41 = newbeta0 + (newbeta1 * newpred_Hr41) + (newbeta2 * newpred_Lr41);
            predictionarray[k] = Double.parseDouble(df.format(newpred_T41));
        }
        return predictionarray;

    }

}
