package Misc;

import Algorithms.ARIMA_Dual_File;
import Algorithms.ARIMA_Single_File;
import Algorithms.MLR_Dual_File;
import Algorithms.MLR_Single_File;
import FileUtils.CSVReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import static java.time.LocalDateTime.now;

public class DatabaseAnalyser extends Thread {
    private final static Logger LOGGER = Logger.getLogger(CSVReader.class.getName());
    private final static HashMap<String, Integer> algorithmMap = new HashMap<>();

    static {
        algorithmMap.put("MLR_Single_File", 0);
        algorithmMap.put("ARIMA_Single_File", 1);
        algorithmMap.put("MLR_Dual_File", 2);
        algorithmMap.put("ARIMA_Dual_File", 3);
    }

    private final int window;
    private final String algorithmName;
    public double[][] variables_Database_1, variables_Database_2;
    public double[] error = new double[7];
    private int ptime, loopTotal;

    public DatabaseAnalyser(String algorithm, int window, double[][] variables_Database_1, double[][] variables_Database_2, int ptime) {
        this.algorithmName = algorithm;
        this.window = window;
        this.variables_Database_1 = variables_Database_1;
        this.variables_Database_2 = variables_Database_2;
        this.ptime = ptime;
    }

    public void analyseDatabase() {

        double[] temperature = variables_Database_1[0];
        double[] humidity = variables_Database_1[1];
        double[] luminosity = variables_Database_1[2];
        double[] rainfall = variables_Database_1[3];
        double[] pressure = variables_Database_1[4];
        double[] wind_dir = variables_Database_1[5];
        double[] wind_speed = variables_Database_1[6];

        double[] temperature2 = variables_Database_2[0];
        double[] humidity2 = variables_Database_2[1];
        double[] luminosity2 = variables_Database_2[2];
        double[] rainfall2 = variables_Database_2[3];
        double[] pressure2 = variables_Database_2[4];
        double[] wind_dir2 = variables_Database_2[5];
        double[] wind_speed2 = variables_Database_2[6];


        int length = temperature.length;
        int end = window + 1;
        int samplingRate = 1;
        int algorithmKey = algorithmMap.get(algorithmName);
        int loops = length - window - ptime - 1;
        this.loopTotal = loops;

        for (int i = 0; i < loops; i++, end++) {
            int index = (samplingRate * ptime) + window + i - 2;

            double[] predictedVariables;
            double[] actualVariables;
            double[] temperature_window = Arrays.copyOfRange(temperature, i, end);
            double[] humidity_window = Arrays.copyOfRange(humidity, i, end);
            double[] light_intensity_window = Arrays.copyOfRange(luminosity, i, end);
            double[] rainfall_window = Arrays.copyOfRange(rainfall, i, end);
            double[] atmospheric_pressure_window = Arrays.copyOfRange(pressure, i, end);
            double[] wind_direction_window = Arrays.copyOfRange(wind_dir, i, end);
            double[] wind_speed_window = Arrays.copyOfRange(wind_speed, i, end);

            if (algorithmKey > 1) {
                double[] temperature_window2 = Arrays.copyOfRange(temperature2, i, end);
                double[] humidity_window2 = Arrays.copyOfRange(humidity2, i, end);
                double[] light_intensity_window2 = Arrays.copyOfRange(luminosity2, i, end);
                double[] rainfall_window2 = Arrays.copyOfRange(rainfall2, i, end);
                double[] atmospheric_pressure_window2 = Arrays.copyOfRange(pressure2, i, end);
                double[] wind_direction_window2 = Arrays.copyOfRange(wind_dir2, i, end);
                double[] wind_speed_window2 = Arrays.copyOfRange(wind_speed2, i, end);

                switch (algorithmKey) {
                    case 2://MLR_Dual_File
                        MLR_Dual_File DualRegression = new MLR_Dual_File(
                                temperature_window,
                                humidity_window,
                                light_intensity_window,
                                rainfall_window,
                                atmospheric_pressure_window,
                                wind_direction_window,
                                wind_speed_window,
                                temperature_window2,
                                humidity_window2,
                                light_intensity_window2,
                                rainfall_window2,
                                atmospheric_pressure_window2,
                                wind_direction_window2,
                                wind_speed_window2
                        );
                        predictedVariables = DualRegression.predict(ptime, window);
                        actualVariables = new double[]{
                                temperature[index],
                                humidity[index],
                                luminosity[index],
                                rainfall[index],
                                pressure[index],
                                wind_dir[index],
                                wind_speed[index]};
                        if ((i % 1000) == 0) {
                            System.out.println(i);
                            printError(error, loops);
                        }
                        for (int j = 0; j < 7; j++) {
                            double predicted = predictedVariables[j];
                            double actual = actualVariables[j];
                            if (actual == 0) {
                                error[j] -= 1;
                            } else {
                                error[j] += Math.abs((predicted - actual) / actual) * 100;
                            }
                        }
                        break;
                    case 3://ARIMA_Dual_File
                        ARIMA_Dual_File DualArima = new ARIMA_Dual_File(
                                temperature_window,
                                humidity_window,
                                light_intensity_window,
                                rainfall_window,
                                atmospheric_pressure_window,
                                wind_direction_window,
                                wind_speed_window,
                                temperature_window2,
                                humidity_window2,
                                light_intensity_window2,
                                rainfall_window2,
                                atmospheric_pressure_window2,
                                wind_direction_window2,
                                wind_speed_window2
                        );
                        predictedVariables = DualArima.predict(ptime, window);
                        int ind = (samplingRate * ptime) + window + i - 2;
                        actualVariables = new double[]{
                                temperature[ind],
                                humidity[ind],
                                luminosity[ind],
                                rainfall[ind],
                                pressure[ind],
                                wind_dir[ind],
                                wind_speed[ind]};
                        if ((i % 1000) == 0) {
                            System.out.println(i + " - " + now().toString());
                            printError(error, loops);
                        }
                        for (int j = 0; j < 7; j++) {
                            double predicted = predictedVariables[j];
                            double actual = actualVariables[j];
                            if (actual == 0) {
                                error[j] -= 1;
                            } else {
                                error[j] += Math.abs((predicted - actual) / actual) * 100;
                            }
                        }
                        break;
                }
            } else {
                switch (algorithmKey) {
                    case 0://MLR_Single_File
                        MLR_Single_File regression = new MLR_Single_File(
                                temperature_window,
                                humidity_window,
                                light_intensity_window,
                                rainfall_window,
                                atmospheric_pressure_window,
                                wind_direction_window,
                                wind_speed_window);

                        predictedVariables = regression.predict(ptime, window, samplingRate);
                        actualVariables = new double[]{
                                temperature[index],
                                humidity[index],
                                luminosity[index],
                                rainfall[index],
                                pressure[index],
                                wind_dir[index],
                                wind_speed[index]};

                        for (int j = 0; j < 7; j++) {
                            double predicted = predictedVariables[j];
                            double actual = actualVariables[j];
                            if (actual == 0) {
                                error[j] -= 1;
                            } else {
                                error[j] += Math.abs((predicted - actual) / actual) * 100;
                            }
                        }
                        break;
                    case 1://ARIMA_Single_File
                        ARIMA_Single_File ARIMA = new ARIMA_Single_File(
                                temperature_window,
                                humidity_window,
                                light_intensity_window,
                                rainfall_window,
                                atmospheric_pressure_window,
                                wind_direction_window,
                                wind_speed_window);
                        predictedVariables = ARIMA.predict(ptime, window);
                        actualVariables = new double[]{
                                temperature[index],
                                humidity[index],
                                luminosity[index],
                                rainfall[index],
                                pressure[index],
                                wind_dir[index],
                                wind_speed[index]};
                        for (int j = 0; j < 7; j++) {
                            double predicted = predictedVariables[j];
                            double actual = actualVariables[j];
                            if (actual == 0) {
                                error[j] -= 1;
                            } else {
                                error[j] += Math.abs((predicted - actual) / actual) * 100;
                            }
                        }
                        break;
                    default:
                        LOGGER.info("Algorithm Does Not Exists.");
                }
            }

        }
    }

    private void printError(double[] error, int loops) {
        System.out.println("Error uing Algorithm: " + algorithmName);
        System.out.println("Temperature Error   : " + error[0] / loops);
        System.out.println("Humidity Error      : " + error[1] / loops);
        System.out.println("Luminosity Error    : " + error[2] / loops);
        System.out.println("Rainfall Error      : " + error[3] / loops);
        System.out.println("Pressure Error      : " + error[4] / loops);
        System.out.println("Wind_Direction Error: " + error[5] / loops);
        System.out.println("Wind_Speed Error    : " + error[6] / loops + "\n\n");
    }

    @Override
    public void run() {
        analyseDatabase();
    }

    public double[] getError() {
        for (int i = 0; i < 7; i++) {
            error[i] = error[i] / loopTotal;
        }
        System.out.println("Error Normalized: " + Arrays.toString(error));
        return error;
    }
}
