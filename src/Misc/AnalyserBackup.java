package Misc;

import Algorithms.ARIMA_Dual_File;
import Algorithms.ARIMA_Single_File;
import Algorithms.MLR_Dual_File;
import Algorithms.MLR_Single_File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AnalyserBackup {

    static double[][] errorarray = new double[15][7];
    static int count32 = 0;
    static int numofsamples = 0;
    static int w = 0;
    static String algo = "";
    private static double[] percent;
    double[][] finalpercentageerror = new double[3][7];
    FileOutputStream fout = null;
    Writer wr = null;

    public AnalyserBackup(String algo, int numofsamples, int window, double[] temperature11, double[] humidity11, double[] light_intensity11, double[] rainfall11, double[] atmospheric_pressure11, double[] wind_direction11, double[] wind_speed11) {
        DecimalFormat df2 = new DecimalFormat("#.#");
        df2.setRoundingMode(RoundingMode.DOWN);
        AnalyserBackup.numofsamples = numofsamples;
        AnalyserBackup.w = window;
        AnalyserBackup.algo = algo;
        try {
            fout = new FileOutputStream("actual-predicted2.txt");
            wr = new OutputStreamWriter(fout);
        } catch (IOException e) {
            System.out.println(e);
        }

        double[] temperature1 = new double[numofsamples];
        double[] humidity1 = new double[numofsamples];
        double[] light_intensity1 = new double[numofsamples];
        double[] rainfall1 = new double[numofsamples];
        double[] atmospheric_pressure1 = new double[numofsamples];
        double[] wind_direction1 = new double[numofsamples];
        double[] wind_speed1 = new double[numofsamples];

        for (int i = 0; i < numofsamples; i++) {
            temperature1[i] = temperature11[i];
            humidity1[i] = humidity11[i];
            light_intensity1[i] = light_intensity11[i];
            rainfall1[i] = rainfall11[i];
            atmospheric_pressure1[i] = atmospheric_pressure11[i];
            wind_direction1[i] = wind_direction11[i];
            wind_speed1[i] = wind_speed11[i];
        }
        int rank = 0;
        for (int ptime = 20; ptime < 61; ) {
            int numofloops = temperature1.length - ptime - w;
            double numofthreads = 15;
            int loops = (numofloops % numofthreads) == 0 ? (int) (numofloops / numofthreads) : (int) (numofloops / numofthreads) + 1;

            ThreadDemo T1 = new ThreadDemo(algo, 0, loops, w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T1.start();

            ThreadDemo T2 = new ThreadDemo(algo, loops, 2 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T2.start();

            ThreadDemo T3 = new ThreadDemo(algo, 2 * (loops), 3 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T3.start();

            ThreadDemo T4 = new ThreadDemo(algo, 3 * (loops), 4 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T4.start();

            ThreadDemo T5 = new ThreadDemo(algo, 4 * (loops), 5 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T5.start();

            ThreadDemo T6 = new ThreadDemo(algo, 5 * (loops), 6 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T6.start();

            ThreadDemo T7 = new ThreadDemo(algo, 6 * (loops), 7 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T7.start();

            ThreadDemo T8 = new ThreadDemo(algo, 7 * (loops), 8 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T8.start();

            ThreadDemo T9 = new ThreadDemo(algo, 8 * (loops), 9 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T9.start();

            ThreadDemo T10 = new ThreadDemo(algo, 9 * (loops), 10 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T10.start();

            ThreadDemo T11 = new ThreadDemo(algo, 10 * (loops), 11 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T11.start();

            ThreadDemo T12 = new ThreadDemo(algo, 11 * (loops), 12 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T12.start();

            ThreadDemo T13 = new ThreadDemo(algo, 12 * (loops), 13 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T13.start();

            ThreadDemo T14 = new ThreadDemo(algo, 13 * (loops), 14 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T14.start();

            ThreadDemo T15 = new ThreadDemo(algo, 14 * (loops), numofloops, w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1);
            T15.start();

            while (true) {
                if (count32 == numofthreads) {
                    for (int i = 0; i < 7; i++) {
                        double averagepercenterr = 0;
                        double sum = 0;
                        for (int j = 0; j < numofthreads; j++) {
                            sum += errorarray[j][i];

                        }
                        averagepercenterr = sum / numofthreads;
                        finalpercentageerror[rank][i] = averagepercenterr;//change sa array la...fer li vin 2 dimension lrla to fill pu 3 time la
                        System.out.println("sum " + sum);
                        switch (i) {
                            case (0):
                                System.out.println("Final Average percentage error(Temperature)= " + df2.format(averagepercenterr));
                                break;
                            case (1):
                                System.out.println("Final Average percentage error(humidity)= " + df2.format(averagepercenterr));
                                break;
                            case (2):
                                System.out.println("Final Average percentage error(light_intensity)= " + df2.format(averagepercenterr));
                                break;
                            case (3):
                                System.out.println("Final Average percentage error(rainfall)= " + df2.format(averagepercenterr));
                                break;
                            case (4):
                                System.out.println("Final Average percentage error(atmospheric_pressure)= " + df2.format(averagepercenterr));
                                break;
                            case (5):
                                System.out.println("Final Average percentage error(wind_direction)= " + df2.format(averagepercenterr));
                                break;
                            case (6):
                                System.out.println("Final Average percentage error(wind_speed)= " + df2.format(averagepercenterr));
                                break;

                        }
                    }
                    break;
                }
            }
            System.out.println("for Ptime= " + ptime);
            ptime = ptime + 20;
            count32 = 0;
            rank++;
        }
    }

    public AnalyserBackup(String algo, int numofsamples, int window, double[] temperature11, double[] humidity11, double[] light_intensity11, double[] rainfall11, double[] atmospheric_pressure11, double[] wind_direction11, double[] wind_speed11, double[] temperature22, double[] humidity22, double[] light_intensity22, double[] rainfall22, double[] atmospheric_pressure22, double[] wind_direction22, double[] wind_speed22) {

        DecimalFormat df2 = new DecimalFormat("#.#");
        df2.setRoundingMode(RoundingMode.DOWN);
        AnalyserBackup.numofsamples = numofsamples;
        AnalyserBackup.w = window;
        AnalyserBackup.algo = algo;
        System.out.println("second constructor...");

        double[] temperature1 = new double[numofsamples];
        double[] humidity1 = new double[numofsamples];
        double[] light_intensity1 = new double[numofsamples];
        double[] rainfall1 = new double[numofsamples];
        double[] atmospheric_pressure1 = new double[numofsamples];
        double[] wind_direction1 = new double[numofsamples];
        double[] wind_speed1 = new double[numofsamples];

        double[] temperature2 = new double[numofsamples];
        double[] humidity2 = new double[numofsamples];
        double[] light_intensity2 = new double[numofsamples];
        double[] rainfall2 = new double[numofsamples];
        double[] atmospheric_pressure2 = new double[numofsamples];
        double[] wind_direction2 = new double[numofsamples];
        double[] wind_speed2 = new double[numofsamples];

        for (int i = 0; i < numofsamples; i++) {

            temperature1[i] = temperature11[i];
            humidity1[i] = humidity11[i];
            light_intensity1[i] = light_intensity11[i];
            rainfall1[i] = rainfall11[i];
            atmospheric_pressure1[i] = atmospheric_pressure11[i];
            wind_direction1[i] = wind_direction11[i];
            wind_speed1[i] = wind_speed11[i];

            temperature2[i] = temperature22[i];
            humidity2[i] = humidity22[i];
            light_intensity2[i] = light_intensity22[i];
            rainfall2[i] = rainfall22[i];
            atmospheric_pressure2[i] = atmospheric_pressure22[i];
            wind_direction2[i] = wind_direction22[i];
            wind_speed2[i] = wind_speed22[i];

        }

        int rank = 0;

        for (int ptime = 20; ptime < 61; ) {

            int numofloops = temperature1.length - ptime - w;

            double numofthreads = 15;
            int loops = (numofloops % numofthreads) == 0 ? (int) (numofloops / numofthreads) : (int) (numofloops / numofthreads) + 1;

            ThreadDemo T1 = new ThreadDemo(algo, 0, loops, w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T1.start();

            ThreadDemo T2 = new ThreadDemo(algo, loops, 2 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T2.start();

            ThreadDemo T3 = new ThreadDemo(algo, 2 * (loops), 3 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T3.start();

            ThreadDemo T4 = new ThreadDemo(algo, 3 * (loops), 4 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T4.start();

            ThreadDemo T5 = new ThreadDemo(algo, 4 * (loops), 5 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T5.start();

            ThreadDemo T6 = new ThreadDemo(algo, 5 * (loops), 6 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T6.start();

            ThreadDemo T7 = new ThreadDemo(algo, 6 * (loops), 7 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T7.start();

            ThreadDemo T8 = new ThreadDemo(algo, 7 * (loops), 8 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T8.start();

            ThreadDemo T9 = new ThreadDemo(algo, 8 * (loops), 9 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T9.start();

            ThreadDemo T10 = new ThreadDemo(algo, 9 * (loops), 10 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T10.start();

            ThreadDemo T11 = new ThreadDemo(algo, 10 * (loops), 11 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T11.start();

            ThreadDemo T12 = new ThreadDemo(algo, 11 * (loops), 12 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T12.start();

            ThreadDemo T13 = new ThreadDemo(algo, 12 * (loops), 13 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T13.start();

            ThreadDemo T14 = new ThreadDemo(algo, 13 * (loops), 14 * (loops), w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T14.start();

            ThreadDemo T15 = new ThreadDemo(algo, 14 * (loops), numofloops, w, ptime, temperature1, humidity1, light_intensity1, rainfall1, atmospheric_pressure1, wind_direction1, wind_speed1, temperature2, humidity2, light_intensity2, rainfall2, atmospheric_pressure2, wind_direction2, wind_speed2);
            T15.start();

            while (true) {
                if (count32 == numofthreads) {
                    for (int i = 0; i < 7; i++) {
                        double averagepercenterr = 0;
                        double sum = 0;
                        for (int j = 0; j < numofthreads; j++) {
                            sum += errorarray[j][i];

                        }
                        averagepercenterr = sum / numofthreads;
                        finalpercentageerror[rank][i] = averagepercenterr;//change sa array la...fer li vin 2 dimension lrla to fill pu 3 time la
                        System.out.println("sum " + sum);
                        switch (i) {
                            case (0):
                                System.out.println("Final Average percentage error(Temperature)= " + df2.format(averagepercenterr));
                                break;
                            case (1):
                                System.out.println("Final Average percentage error(humidity)= " + df2.format(averagepercenterr));
                                break;
                            case (2):
                                System.out.println("Final Average percentage error(light_intensity)= " + df2.format(averagepercenterr));
                                break;
                            case (3):
                                System.out.println("Final Average percentage error(rainfall)= " + df2.format(averagepercenterr));
                                break;
                            case (4):
                                System.out.println("Final Average percentage error(atmospheric_pressure)= " + df2.format(averagepercenterr));
                                break;
                            case (5):
                                System.out.println("Final Average percentage error(wind_direction)= " + df2.format(averagepercenterr));
                                break;
                            case (6):
                                System.out.println("Final Average percentage error(wind_speed)= " + df2.format(averagepercenterr));
                                break;

                        }
                    }
                    break;
                }
            }
            System.out.println("for Ptime= " + ptime);
            ptime = ptime + 20;
            count32 = 0;
            rank++;
        }
    }

    public static void fill(double[] percent) {
        AnalyserBackup.percent = percent;
        System.out.println("filling " + count32);
        System.arraycopy(percent, 0, errorarray[count32], 0, 7);
        count32++;
    }

    public double[][] getPercentageError() {
        return finalpercentageerror;
    }

    static class ThreadDemo extends Thread {

        int start;
        int end;
        int count1 = 0;
        int w = 0;
        double[] percent = new double[7];
        double[] temperature1;
        double[] humidity1;
        double[] light_intensity1;
        double[] rainfall1;
        double[] atmospheric_pressure1;
        double[] wind_direction1;
        double[] wind_speed1;
        double[] temperature2;
        double[] humidity2;
        double[] light_intensity2;
        double[] rainfall2;
        double[] atmospheric_pressure2;
        double[] wind_direction2;
        double[] wind_speed2;
        double[][] errorarray;
        int ptime = 0;
        String algo = "";
        private Thread t;

        ThreadDemo(String algo, int start, int end, int window, int ptime, double[] temperature, double[] humidity, double[] light_intensity,
                   double[] rainfall, double[] pressure, double[] wind_direction, double[] wind_speed) {
            this.start = start;
            this.end = end;
            this.w = window;
            this.algo = algo;
            System.out.println("first thread constructor...");
            this.ptime = ptime;
            this.temperature1 = temperature;
            this.humidity1 = humidity;
            this.light_intensity1 = light_intensity;
            this.rainfall1 = rainfall;
            this.atmospheric_pressure1 = pressure;
            this.wind_direction1 = wind_direction;
            this.wind_speed1 = wind_speed;
            errorarray = new double[end - start][7];
            System.out.println("start= " + start);
            System.out.println("end= " + end);

        }

        ThreadDemo(String algo, int start, int end, int window, int ptime,
                   double[] temperature, double[] humidity, double[] light_intensity, double[] rainfall, double[] pressure,
                   double[] wind_direction, double[] wind_speed,
                   double[] temperature2, double[] humidity2,
                   double[] light_intensity2, double[] rainfall2, double[] pressure2, double[] wind_direction2,
                   double[] wind_speed2) {
            this.start = start;
            this.end = end;
            this.w = window;
            this.algo = algo;
            System.out.println("second thread constructor...");
            this.ptime = ptime;
            this.temperature1 = temperature;
            this.humidity1 = humidity;
            this.light_intensity1 = light_intensity;
            this.rainfall1 = rainfall;
            this.atmospheric_pressure1 = pressure;
            this.wind_direction1 = wind_direction;
            this.wind_speed1 = wind_speed;

            this.temperature2 = temperature2;
            this.humidity2 = humidity2;
            this.light_intensity2 = light_intensity2;
            this.rainfall2 = rainfall2;
            this.atmospheric_pressure2 = pressure2;
            this.wind_direction2 = wind_direction2;
            this.wind_speed2 = wind_speed2;

            errorarray = new double[end - start][7];
            System.out.println("start= " + start);
            System.out.println("end= " + end);
        }

        @Override
        public void run() {
            int count = 0;
            int count2 = 0;
            for (int j = start; j < end; j++) {
                count1++;
                double[] temperature = null;
                double[] humidity = null;
                double[] light_intensity = null;
                double[] rainfall = null;
                double[] atmospheric_pressure = null;
                double[] wind_direction = null;
                double[] wind_speed = null;

                if (algo.equals("MLR_Single_File") || algo.equals("MLR_Dual_File")) {
                    temperature = new double[w + 1];
                    humidity = new double[w + 1];
                    light_intensity = new double[w + 1];
                    rainfall = new double[w + 1];
                    atmospheric_pressure = new double[w + 1];
                    wind_direction = new double[w + 1];
                    wind_speed = new double[w + 1];
                } else {
                    temperature = new double[w];
                    humidity = new double[w];
                    light_intensity = new double[w];
                    rainfall = new double[w];
                    atmospheric_pressure = new double[w];
                    wind_direction = new double[w];
                    wind_speed = new double[w];
                }

                double[] temperature22 = null;
                double[] humidity22 = null;
                double[] light_intensity22 = null;
                double[] rainfall22 = null;
                double[] atmospheric_pressure22 = null;
                double[] wind_direction22 = null;
                double[] wind_speed22 = null;

                if (algo.equals("MLR_Dual_File")) {
                    temperature22 = new double[w + 1];
                    humidity22 = new double[w + 1];
                    light_intensity22 = new double[w + 1];
                    rainfall22 = new double[w + 1];
                    atmospheric_pressure22 = new double[w + 1];
                    wind_direction22 = new double[w + 1];
                    wind_speed22 = new double[w + 1];
                } else {
                    temperature22 = new double[w];
                    humidity22 = new double[w];
                    light_intensity22 = new double[w];
                    rainfall22 = new double[w];
                    atmospheric_pressure22 = new double[w];
                    wind_direction22 = new double[w];
                    wind_speed22 = new double[w];
                }

                if (algo.equals("MLR_Single_File") || algo.equals("MLR_Dual_File")) {
                    for (int i = 0; i <= w; i++) {
                        temperature[i] = temperature1[i + j];
                        humidity[i] = humidity1[i + j];
                        light_intensity[i] = light_intensity1[i + j];
                        rainfall[i] = rainfall1[i + j];
                        atmospheric_pressure[i] = atmospheric_pressure1[i + j];
                        wind_direction[i] = wind_direction1[i + j];
                        wind_speed[i] = wind_speed1[i + j];

                        if (algo.equals("MLR_Dual_File")) {
                            temperature22[i] = temperature2[i + j];
                            humidity22[i] = humidity2[i + j];
                            light_intensity22[i] = light_intensity2[i + j];
                            rainfall22[i] = rainfall2[i + j];
                            atmospheric_pressure22[i] = atmospheric_pressure2[i + j];
                            wind_direction22[i] = wind_direction2[i + j];
                            wind_speed22[i] = wind_speed2[i + j];

                        }

                    }

                } else {
                    for (int i = 0; i < w; i++) {
                        temperature[i] = temperature1[i + j];
                        humidity[i] = humidity1[i + j];
                        light_intensity[i] = light_intensity1[i + j];
                        rainfall[i] = rainfall1[i + j];
                        atmospheric_pressure[i] = atmospheric_pressure1[i + j];
                        wind_direction[i] = wind_direction1[i + j];
                        wind_speed[i] = wind_speed1[i + j];
                        if (algo.equals("ARIMA_Dual_File")) {
                            temperature22[i] = temperature2[i + j];
                            humidity22[i] = humidity2[i + j];
                            light_intensity22[i] = light_intensity2[i + j];
                            rainfall22[i] = rainfall2[i + j];
                            atmospheric_pressure22[i] = atmospheric_pressure2[i + j];
                            wind_direction22[i] = wind_direction2[i + j];
                            wind_speed22[i] = wind_speed2[i + j];
                        }

                    }
                }
                double[] predictionarray = new double[7];

                if (algo.equals("ARIMA_Single_File")) {
                    ARIMA_Single_File a1 = new ARIMA_Single_File(temperature, humidity, light_intensity, rainfall, atmospheric_pressure, wind_direction, wind_speed);

                    try {
                        predictionarray = a1.predict(ptime, w);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (algo.equals("MLR_Single_File")) {
                    MLR_Single_File a1 = new MLR_Single_File(temperature, humidity, light_intensity, rainfall, atmospheric_pressure, wind_direction, wind_speed);

                    predictionarray = a1.predict(ptime, w, 1);
                } else if (algo.equals("MLR_Dual_File")) {
                    MLR_Dual_File a1 = new MLR_Dual_File(temperature, humidity, light_intensity, rainfall, atmospheric_pressure, wind_direction, wind_speed, temperature22, humidity22, light_intensity22, rainfall22, atmospheric_pressure22, wind_direction22, wind_speed22);
                    predictionarray = a1.predict(ptime, w);
                } else if (algo.equals("ARIMA_Dual_File")) {
                    ARIMA_Dual_File a1 = new ARIMA_Dual_File(temperature, humidity, light_intensity, rainfall, atmospheric_pressure, wind_direction, wind_speed, temperature22, humidity22, light_intensity22, rainfall22, atmospheric_pressure22, wind_direction22, wind_speed22);

                    predictionarray = a1.predict(ptime, w);
                }

                double[] actualvalue = new double[7];
                actualvalue[0] = temperature1[w + j + ptime];
                actualvalue[1] = humidity1[w + j + ptime];
                actualvalue[2] = light_intensity1[w + j + ptime];
                actualvalue[3] = rainfall1[w + j + ptime];
                actualvalue[4] = atmospheric_pressure1[w + j + ptime];
                actualvalue[5] = wind_direction1[w + j + ptime];
                actualvalue[6] = wind_speed1[w + j + ptime];

                for (int c = 0; c < 7; c++) {
                    double percenterr = 0;
                    if (actualvalue[c] != 0.0) {
                        percenterr = ((Math.abs(predictionarray[c] - actualvalue[c])) / actualvalue[c]) * 100;
                    } else {
                        percenterr = -1;
                    }
                    errorarray[count][c] = percenterr;
                }
                DecimalFormat df2 = new DecimalFormat("#.#");
                df2.setRoundingMode(RoundingMode.DOWN);
                count++;

            }
            for (int i = 0; i < 7; i++) {
                count2 = 0;
                double averagepercenterr = 0;
                double sum = 0;
                for (int j = 0; j < end - start; j++) {
                    if (errorarray[j][i] >= 0) {
                        sum += errorarray[j][i];
                    } else {
                        count2++;
                    }
                }
                System.out.println("count= " + count2);
                averagepercenterr = sum / (errorarray.length - count2);
                percent[i] = averagepercenterr;
            }
            AnalyserBackup.fill(percent);
            System.out.println("count1= " + count1);
        }

        @Override
        public void start() {
            if (t == null) {
                t = new Thread(this);
                t.start();
            }
        }
    }
}
