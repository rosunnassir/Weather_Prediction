package Algorithms;

import ARIMA.ARIMA1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class ARIMA_Single_File {

    double[] temperatureOriginal;
    double[] humidityOriginal;
    double[] luminosity_Original;
    double[] rainfall_Original;
    double[] pressure_Original;
    double[] wind_direction_Original;
    double[] wind_speed_Original;


    int ptime;

    public ARIMA_Single_File(double[] temperature, double[] humidity, double[] light_intensity,
                             double[] rainfall, double[] pressure, double[] wind_direction,
                             double[] wind_speed) {
        this.temperatureOriginal = temperature;
        this.humidityOriginal = humidity;
        this.luminosity_Original = light_intensity;
        this.rainfall_Original = rainfall;
        this.pressure_Original = pressure;
        this.wind_direction_Original = wind_direction;
        this.wind_speed_Original = wind_speed;
    }


    public double[] predict(int ptime, int window) {
        double predictTemp = 0, predictHumid = 0, predictLumen = 0,
                predictRain = 0, predictPressure = 0, predictWin_Dir = 0, predictWind_Speed = 0;

        try {
            this.ptime = ptime;
            int index = (temperatureOriginal.length - window);
            int last = temperatureOriginal.length;

            //Fetches last W - values from input array
            double[] temperature = Arrays.copyOfRange(temperatureOriginal, index, last);
            double[] humidity = Arrays.copyOfRange(humidityOriginal, index, last);
            double[] luminosity = Arrays.copyOfRange(luminosity_Original, index, last);
            double[] rainfall = Arrays.copyOfRange(rainfall_Original, index, last);
            double[] pressure = Arrays.copyOfRange(pressure_Original, index, last);
            double[] wind_direction = Arrays.copyOfRange(wind_direction_Original, index, last);
            double[] wind_speed = Arrays.copyOfRange(wind_speed_Original, index, last);

            FileOutputStream fileOutputStream;
            fileOutputStream = new FileOutputStream("text.txt");
            OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutputStream);
            streamWriter.write("                                            Forecast values\n");
            streamWriter.flush();
            streamWriter.write("Temperature    Humidity    Light_Intensity    Rainfall    Atmospheric_Pressure    Wind_Direction    Wind_Speed\n\n");
            streamWriter.flush();

            //index is 2 -> need to fetch w+1 values where w+1 = 0.0 -> start from 2 upto w+1
            index = 2;
            //last index greater than array length -> last null value replaced by prediction
            last = temperatureOriginal.length + 1;

            for (int i = 0; i < ptime; i++, index++) {

                //uses ARIMA to predict next value based on last W-values
                ARIMA1 tempPred = new ARIMA1(temperature);
                ARIMA1 humidPred = new ARIMA1(humidity);
                ARIMA1 rainPred = new ARIMA1(luminosity);
                ARIMA1 lumenPred = new ARIMA1(rainfall);
                ARIMA1 pressurePred = new ARIMA1(pressure);
                ARIMA1 win_dir_Pred = new ARIMA1(wind_direction);
                ARIMA1 wind_spd_Pred = new ARIMA1(wind_speed);

                //Store predicted values here for replacement later
                predictTemp = tempPred.predict(tempPred);
                predictHumid = humidPred.predict(humidPred);
                predictLumen = lumenPred.predict(lumenPred);
                predictRain = rainPred.predict(rainPred);
                predictPressure = pressurePred.predict(pressurePred);
                predictWin_Dir = win_dir_Pred.predict(win_dir_Pred);
                predictWind_Speed = wind_spd_Pred.predict(wind_spd_Pred);

                //copies last W-values but starting from position 2 upto W+1 where w+1 does not exist and is signed as 0.0
                //get last value index
                //Then set the predict value as last element-> array of w-1 values from original + last value is predicted val.
                temperature = Arrays.copyOfRange(temperatureOriginal, index, last);
                int end = temperature.length - 1;
                temperature[end] = predictTemp;
                humidity = Arrays.copyOfRange(humidityOriginal, index, last);
                humidity[end] = predictHumid;
                luminosity = Arrays.copyOfRange(luminosity_Original, index, last);
                luminosity[end] = predictLumen;
                rainfall = Arrays.copyOfRange(rainfall_Original, index, last);
                rainfall[end] = predictRain;
                pressure = Arrays.copyOfRange(pressure_Original, index, last);
                pressure[end] = predictPressure;
                wind_direction = Arrays.copyOfRange(wind_direction_Original, index, last);
                wind_direction[end] = predictWin_Dir;
                wind_speed = Arrays.copyOfRange(wind_speed_Original, index, last);
                wind_speed[end] = predictWind_Speed;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new double[]{predictTemp, predictHumid, predictLumen, predictRain,
                predictPressure, predictWin_Dir, predictWind_Speed};
    }

}
