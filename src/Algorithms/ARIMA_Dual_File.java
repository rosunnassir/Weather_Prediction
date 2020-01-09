package Algorithms;

import ARIMA.ARIMA2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

public class ARIMA_Dual_File {

    double[] temperature_first;
    double[] humidity_first;
    double[] luminosity_first;
    double[] rainfall_first;
    double[] pressure_first;
    double[] wind_dir__first;
    double[] wind_speed__first;

    double[] temperature_second;
    double[] humidity_second;
    double[] luminosity_second;
    double[] rainfall_second;
    double[] pressure_second;
    double[] wind_dir_second;
    double[] wind_speed__second;


    int window;
    int ptime;

    public ARIMA_Dual_File(double[] temperature, double[] humidity, double[] luminosity, double[] rainfall, double[] pressure, double[] wind_direction, double[] wind_speed, double[] temperature2, double[] humidity2, double[] light_intensity2, double[] rainfall2, double[] pressure2, double[] wind_direction2, double[] wind_speed2) {

        this.temperature_first = temperature;
        this.humidity_first = humidity;
        this.luminosity_first = luminosity;
        this.rainfall_first = rainfall;
        this.pressure_first = pressure;
        this.wind_dir__first = wind_direction;
        this.wind_speed__first = wind_speed;
        this.temperature_second = temperature2;
        this.humidity_second = humidity2;
        this.luminosity_second = light_intensity2;
        this.rainfall_second = rainfall2;
        this.pressure_second = pressure2;
        this.wind_dir_second = wind_direction2;
        this.wind_speed__second = wind_speed2;

    }

    public double[] predict(int ptime, int window) {
        double predictTemp = 0, predictHumid = 0, predictLumen = 0, predictRain = 0, predictPressure = 0, predictWin_Dir = 0, predictWind_Speed = 0;
        double predictTemp2 = 0, predictHumid2 = 0, predictLumen2 = 0, predictRain2 = 0, predictPressure2 = 0, predictWin_Dir2 = 0, predictWind_Speed2 = 0;

        double[] predicted = new double[7];

        try {
            this.ptime = ptime;
            this.window = window;

            int index = (temperature_first.length - window);
            int last = temperature_first.length;

            double[] temperature = Arrays.copyOfRange(temperature_first, index, last);
            double[] humidity = Arrays.copyOfRange(humidity_first, index, last);
            double[] luminosity = Arrays.copyOfRange(luminosity_first, index, last);
            double[] rainfall = Arrays.copyOfRange(rainfall_first, index, last);
            double[] pressure = Arrays.copyOfRange(pressure_first, index, last);
            double[] wind_direction = Arrays.copyOfRange(wind_dir__first, index, last);
            double[] wind_speed = Arrays.copyOfRange(wind_speed__first, index, last);

            double[] temperature2 = Arrays.copyOfRange(temperature_second, index, last);
            double[] humidity2 = Arrays.copyOfRange(humidity_second, index, last);
            double[] luminosity2 = Arrays.copyOfRange(luminosity_second, index, last);
            double[] rainfall2 = Arrays.copyOfRange(rainfall_second, index, last);
            double[] pressure2 = Arrays.copyOfRange(pressure_second, index, last);
            double[] wind_direction2 = Arrays.copyOfRange(wind_dir_second, index, last);
            double[] wind_speed2 = Arrays.copyOfRange(wind_speed__second, index, last);


            FileOutputStream fout = new FileOutputStream("text.txt");
            Writer wr = new OutputStreamWriter(fout);
            FileOutputStream fout2 = new FileOutputStream("text2.txt");
            Writer wr2 = new OutputStreamWriter(fout2);
            wr.write("                                       Forecast values(Database1)\n\n");
            wr.flush();
            wr.write("Temperature    Humidity    Light_Intensity    Rainfall    Atmospheric_Pressure    Wind_Direction    Wind_Speed\n\n");
            wr.flush();

            wr2.write("                                       Forecast values(Database2)\n\n");
            wr2.flush();
            wr2.write("Temperature    Humidity    Light_Intensity    Rainfall    Atmospheric_Pressure    Wind_Direction    Wind_Speed\n\n");
            wr2.flush();


            for (int i = 0; i < ptime; i++, index++) {

                //uses ARIMA to predict next value based on last W-values
                ARIMA2 tempPred = new ARIMA2(temperature, temperature2);
                ARIMA2 humidPred = new ARIMA2(humidity, humidity2);
                ARIMA2 rainPred = new ARIMA2(luminosity, luminosity2);
                ARIMA2 lumenPred = new ARIMA2(rainfall, rainfall2);
                ARIMA2 pressurePred = new ARIMA2(pressure, pressure2);
                ARIMA2 win_dir_Pred = new ARIMA2(wind_direction, wind_direction2);
                ARIMA2 wind_spd_Pred = new ARIMA2(wind_speed, wind_speed2);

                //Store predicted values here for replacement later
                predictTemp = tempPred.predict(tempPred);
                predictHumid = humidPred.predict(humidPred);
                predictLumen = lumenPred.predict(lumenPred);
                predictRain = rainPred.predict(rainPred);
                predictPressure = pressurePred.predict(pressurePred);
                predictWin_Dir = win_dir_Pred.predict(win_dir_Pred);
                predictWind_Speed = wind_spd_Pred.predict(wind_spd_Pred);

                predictTemp2 = tempPred.predict2(tempPred);
                predictHumid2 = humidPred.predict2(humidPred);
                predictLumen2 = lumenPred.predict2(lumenPred);
                predictRain2 = rainPred.predict2(rainPred);
                predictPressure2 = pressurePred.predict2(pressurePred);
                predictWin_Dir2 = win_dir_Pred.predict2(win_dir_Pred);
                predictWind_Speed2 = wind_spd_Pred.predict2(wind_spd_Pred);

                //copies last W-values but starting from position 2 upto W+1 where w+1 does not exist and is signed as 0.0
                //get last value index
                //Then set the predict value as last element-> array of w-1 values from original + last value is predicted val.
                temperature = Arrays.copyOfRange(temperature_first, index, last);
                int end = temperature.length - 1;
                temperature[end] = predictTemp;
                humidity = Arrays.copyOfRange(humidity_first, index, last);
                humidity[end] = predictHumid;
                luminosity = Arrays.copyOfRange(luminosity_first, index, last);
                luminosity[end] = predictLumen;
                rainfall = Arrays.copyOfRange(rainfall_first, index, last);
                rainfall[end] = predictRain;
                pressure = Arrays.copyOfRange(pressure_first, index, last);
                pressure[end] = predictPressure;
                wind_direction = Arrays.copyOfRange(wind_dir__first, index, last);
                wind_direction[end] = predictWin_Dir;
                wind_speed = Arrays.copyOfRange(wind_speed__first, index, last);
                wind_speed[end] = predictWind_Speed;

                temperature2 = Arrays.copyOfRange(temperature_second, index, last);
                temperature2[end] = predictTemp2;
                humidity2 = Arrays.copyOfRange(humidity_second, index, last);
                humidity2[end] = predictHumid2;
                luminosity2 = Arrays.copyOfRange(luminosity_second, index, last);
                luminosity2[end] = predictLumen2;
                rainfall2 = Arrays.copyOfRange(rainfall_second, index, last);
                rainfall2[end] = predictRain2;
                pressure2 = Arrays.copyOfRange(pressure_second, index, last);
                pressure2[end] = predictPressure2;
                wind_direction2 = Arrays.copyOfRange(wind_dir_second, index, last);
                wind_direction2[end] = predictWin_Dir2;
                wind_speed2 = Arrays.copyOfRange(wind_speed__second, index, last);
                wind_speed2[end] = predictWind_Speed;

                wr.write(predictTemp + "            " + predictHumid + "            " + predictLumen + "            "
                        + predictRain + "            " + predictPressure + "            " + predictWin_Dir + "            "
                        + predictWind_Speed + "\n\n");
                wr.flush();

                wr.write(predictTemp2 + "            " + predictHumid2 + "            " + predictLumen2 + "            "
                        + predictRain2 + "            " + predictPressure2 + "            " + predictWin_Dir2 + "            "
                        + predictWind_Speed2 + "\n\n");
                wr.flush();

                predicted[0] = predictTemp;
                predicted[1] = predictHumid;
                predicted[2] = predictLumen;
                predicted[3] = predictRain;
                predicted[4] = predictPressure;
                predicted[5] = predictWin_Dir;
                predicted[6] = predictWind_Speed;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return predicted;
    }

}
