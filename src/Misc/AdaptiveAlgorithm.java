package Misc;

import Algorithms.ARIMA_Dual_File;
import Algorithms.ARIMA_Single_File;
import Algorithms.MLR_Dual_File;
import Algorithms.MLR_Single_File;
import FileUtils.CSVReader;

import java.text.DecimalFormat;
import java.util.Arrays;

public class AdaptiveAlgorithm {
    public static void main(String[] args) {
        int window = 500;
        CSVReader firstReader = new CSVReader("Book1.csv", window, false);
        CSVReader secondReader = new CSVReader("Book2.csv", window, false);
        final double[][] variables_Database_1 = getVariablesFromReader(firstReader);
        final double[][] variables_Database_2 = getVariablesFromReader(secondReader);

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

        int end = window / 2;
        double[][] predictedMLRSingle = new double[end][7];
        double[][] predictedMLRDual = new double[end][7];
        double[][] predictedARIMASingle = new double[end][7];
        double[][] predictedARIMADual = new double[end][7];

        for (int i = 0; i < (window / 2) - 1; i++, end++) {
            int ptime = (end + 1);
//            System.out.println("Start: " + end + " Index from: " + i + " Index to: " + end + " Prediction Time: " + ptime);
            double[] temperature_window = Arrays.copyOfRange(temperature, i, end + 1);
            double[] humidity_window = Arrays.copyOfRange(humidity, i, end + 1);
            double[] light_intensity_window = Arrays.copyOfRange(luminosity, i, end + 1);
            double[] rainfall_window = Arrays.copyOfRange(rainfall, i, end + 1);
            double[] atmospheric_pressure_window = Arrays.copyOfRange(pressure, i, end + 1);
            double[] wind_direction_window = Arrays.copyOfRange(wind_dir, i, end + 1);
            double[] wind_speed_window = Arrays.copyOfRange(wind_speed, i, end + 1);

            double[] temperature_window2 = Arrays.copyOfRange(temperature2, i, end + 1);
            double[] humidity_window2 = Arrays.copyOfRange(humidity2, i, end + 1);
            double[] light_intensity_window2 = Arrays.copyOfRange(luminosity2, i, end + 1);
            double[] rainfall_window2 = Arrays.copyOfRange(rainfall2, i, end + 1);
            double[] atmospheric_pressure_window2 = Arrays.copyOfRange(pressure2, i, end + 1);
            double[] wind_direction_window2 = Arrays.copyOfRange(wind_dir2, i, end + 1);
            double[] wind_speed_window2 = Arrays.copyOfRange(wind_speed2, i, end + 1);

            MLR_Single_File regression = new MLR_Single_File(
                    temperature_window,
                    humidity_window,
                    light_intensity_window,
                    rainfall_window,
                    atmospheric_pressure_window,
                    wind_direction_window,
                    wind_speed_window);
            double[] predictRegression = regression.predict(ptime, (window / 2), 1);
            predictedMLRSingle[i] = predictRegression;

            ARIMA_Single_File ARIMA = new ARIMA_Single_File(
                    temperature_window,
                    humidity_window,
                    light_intensity_window,
                    rainfall_window,
                    atmospheric_pressure_window,
                    wind_direction_window,
                    wind_speed_window);
            double[] predictARIMA = ARIMA.predict(1, window / 2);
            predictedARIMASingle[i] = predictARIMA;

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
            double[] predictDualRegression = DualRegression.predict(ptime, (window / 2));
            predictedMLRDual[i] = predictDualRegression;


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
            double[] predictDualArima = DualArima.predict(1, window / 2);
            predictedARIMADual[i] = predictDualArima;
        }
        int len = predictedARIMADual.length;

        for (int i = 0; i < len - 1; i++) {
            double[] data = new double[7];
            for (int j = 0; j < 7; j++) {
                double temp = variables_Database_1[j][i];
                data[j] = temp;
            }
            if (i > len / 16) {
                double[] predictMLRsingle = predictedMLRSingle[i];
                double[] predictMLRDual = predictedMLRDual[i];
                double[] predictARIMAsingle = predictedARIMASingle[i];
                double[] predictARIMAdual = predictedARIMADual[i];
                System.out.println("Actual 1            : " + Arrays.toString(data));

                System.out.println("MLR Single File     : " + Arrays.toString(predictMLRsingle)
                        + "   Square Error: " + Arrays.toString(squaredError(data, predictMLRsingle)));

                System.out.println("ARIMA Single File   : " + Arrays.toString(predictARIMAsingle)
                        + "   Square Error: " + Arrays.toString(squaredError(data, predictARIMAsingle)));

                System.out.println("MLR Dual File       : " + Arrays.toString(predictMLRDual)
                        + "   Square Error: " + Arrays.toString(squaredError(data, predictMLRDual)));

                System.out.println("ARIMA Dual File     : " + Arrays.toString(predictARIMAdual)
                        + "   Square Error: " + Arrays.toString(squaredError(data, predictARIMAdual)));
                System.out.println();
            }
        }

    }

    public static double[] squaredError(double[] actual, double[] predicted) {
        DecimalFormat df = new DecimalFormat("#.#####");
        double[] square = new double[7];
        for (int i = 0; i < actual.length; i++) {
            double actualval = actual[i];
            double predictval = predicted[i];
            if (actualval != 0) {
                double error = Math.abs(actualval - predictval) / actualval;
                error *= error;
                square[i] = Double.parseDouble(df.format(error));
            } else {
                square[i] = 100;
            }
        }
        return square;
    }

    private static double[][] getVariablesFromReader(CSVReader firstReader) {
        double[] temperatureS = firstReader.getTemp();
        double[] humidityS = firstReader.getHumidity();
        double[] light_intensityS = firstReader.getLight();
        double[] rainfallS = firstReader.getRainfall();
        double[] atmospheric_pressureS = firstReader.getPressure();
        double[] wind_directionS = firstReader.getWindDirection();
        double[] wind_speedS = firstReader.getWindSpeed();
        return new double[][]{temperatureS, humidityS, light_intensityS, rainfallS, atmospheric_pressureS,
                wind_directionS, wind_speedS};
    }
}
