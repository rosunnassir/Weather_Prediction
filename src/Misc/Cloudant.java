package Misc;

import FileUtils.CSVReader;
import cloudant.manager.CloudantManager;

import java.util.Arrays;

public class Cloudant {
    public static void main(String[] args) {

        Predictor predictor = new Predictor(50, 1, 45, "MLR_Single_File");
        predictor.predict(true);
        double[] predictedVariables = predictor.getPredictedVariables();
        System.out.println(Arrays.toString(predictedVariables));

        if (false) {
            CloudantManager cloudantManager = new CloudantManager("weather_data_1");
            CSVReader firstReader = new CSVReader("Book1.csv", 0, true);
            double[] temperatureS = firstReader.getTemp();
            double[] humidityS = firstReader.getHumidity();
            double[] light_intensityS = firstReader.getLight();
            double[] rainfallS = firstReader.getRainfall();
            double[] atmospheric_pressureS = firstReader.getPressure();
            double[] wind_directionS = firstReader.getWindDirection();
            double[] wind_speedS = firstReader.getWindSpeed();

            for (int i = 0; i < temperatureS.length - 1; i++) {
                if ((i % 1000) == 0) {
                    System.out.println(i);
                }
                cloudantManager.setTemperature(temperatureS[i]);
                cloudantManager.setHumidity(humidityS[i]);
                cloudantManager.setLight_lvl(light_intensityS[i]);
                cloudantManager.setRain_in(rainfallS[i]);
                cloudantManager.setPressure(atmospheric_pressureS[i]);
                cloudantManager.setWind_dir(wind_directionS[i]);
                cloudantManager.setWind_speed(wind_speedS[i]);
                cloudantManager.addToCloudant();
            }
        }
    }
}

