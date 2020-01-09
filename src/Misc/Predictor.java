package Misc;

import Algorithms.ARIMA_Dual_File;
import Algorithms.ARIMA_Single_File;
import Algorithms.MLR_Dual_File;
import Algorithms.MLR_Single_File;
import FileUtils.CSVReader;
import cloudant.manager.CloudantManager;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Predictor {
    private final static HashMap<String, Integer> algorithmMap = new HashMap<>();

    static {
        algorithmMap.put("MLR_Single_File", 0);
        algorithmMap.put("ARIMA_Single_File", 1);
        algorithmMap.put("MLR_Dual_File", 2);
        algorithmMap.put("ARIMA_Dual_File", 3);
    }

    public double[] predictedVariables = new double[7];
    InputStream path1, path2;
    private String algorithmName;
    private int window, samplingRate, predictTime;

    public Predictor(int window, int samplingRate, int predictTime, String algorithm) {
        this.algorithmName = algorithm;
        this.window = window;
        this.predictTime = predictTime;
        this.samplingRate = samplingRate;
    }

    public void setPath1(InputStream path1) {
        this.path1 = path1;
    }

    public void setPath2(InputStream path2) {
        this.path2 = path2;
    }

    public void predict(boolean cloudant) {
        int algorithmKey = algorithmMap.get(algorithmName);
        int fetch = ((algorithmKey == 0) || (algorithmKey == 2)) ? window + 1 : window;
        double[][] variables_Database_1;
        double[][] variables_Database_2;
        if (cloudant) {
            String database1 = "weather_data_1";
            CloudantManager manager = new CloudantManager(database1);
            manager.fetchData(fetch, database1);
            variables_Database_1 = manager.getVariablesFromCloud();
            variables_Database_2 = manager.getVariablesFromCloud();
        } else {
            CSVReader firstReader = new CSVReader(path1, fetch);
            CSVReader secondReader = new CSVReader(path2, fetch);
            variables_Database_1 = getVariablesFromReader(firstReader);
            variables_Database_2 = getVariablesFromReader(secondReader);
        }
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

        if (algorithmKey > 1) {
            switch (algorithmKey) {
                case 2://MLR_Dual_File
                    MLR_Dual_File DualRegression = new MLR_Dual_File(
                            temperature,
                            humidity,
                            luminosity,
                            rainfall,
                            pressure,
                            wind_dir,
                            wind_speed,
                            temperature2,
                            humidity2,
                            luminosity2,
                            rainfall2,
                            pressure2,
                            wind_dir2,
                            wind_speed2
                    );
                    predictedVariables = DualRegression.predict(predictTime, window);

                    break;
                case 3://ARIMA_Dual_File
                    ARIMA_Dual_File DualArima = new ARIMA_Dual_File(
                            temperature,
                            humidity,
                            luminosity,
                            rainfall,
                            pressure,
                            wind_dir,
                            wind_speed,
                            temperature2,
                            humidity2,
                            luminosity2,
                            rainfall2,
                            pressure2,
                            wind_dir2,
                            wind_speed2
                    );
                    predictedVariables = DualArima.predict(predictTime, window);
                    break;
            }
        } else {
            switch (algorithmKey) {
                case 0://MLR_Single_File
                    MLR_Single_File regression = new MLR_Single_File(
                            temperature,
                            humidity,
                            luminosity,
                            rainfall,
                            pressure,
                            wind_dir,
                            wind_speed
                    );

                    predictedVariables = regression.predict(predictTime, window, samplingRate);
                    break;
                case 1://ARIMA_Single_File
                    ARIMA_Single_File ARIMA = new ARIMA_Single_File(
                            temperature,
                            humidity,
                            luminosity,
                            rainfall,
                            pressure,
                            wind_dir,
                            wind_speed
                    );
                    predictedVariables = ARIMA.predict(predictTime, window);
                    break;
                default:
                    break;
            }
        }
    }

    public double[] getPredictedVariables() {
        DecimalFormat df2 = new DecimalFormat(".#");
        for (int i = 0; i < 7; i++) {
            predictedVariables[i] = Double.parseDouble(df2.format(predictedVariables[i]));
            System.out.println("Variable: " + predictedVariables[i]);
        }
        return predictedVariables;
    }

    private double[][] getVariablesFromReader(CSVReader firstReader) {
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
