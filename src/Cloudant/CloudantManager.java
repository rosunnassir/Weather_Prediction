package Cloudant;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryResult;
import com.google.gson.internal.LinkedTreeMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloudantManager {

    public static CloudantClient client;
    public Database database;
    public double itemsInDb = 0;
    private Double wind_speed;
    private Double wind_dir;
    private Double humidity;
    private Double temperature;
    private Double rain_in;
    private Double pressure;
    private Double light_lvl;
    private double[] windSpeedVector, winDirVector, humidityVector, temperatureVector, rainfallVector, presureVector, light_levelVector;

    public CloudantManager(String databaseName) {
        try {
            client = ClientBuilder.url(new URL("https://43eb75f0-22c6-4020-9c8e-fe5378f1b252-bluemix.cloudant.com"))
                    .username("oonessintyactervandandon")
                    .password("e3a6ce83ecdc26b35e0b86ab4622287b6fa83c34")
                    .build();
            database = client.database(databaseName, false);//connects to database without creating a new one
            System.out.println("Client Connected Successfully.");
            itemsInDb = getItemsInDb();
        } catch (MalformedURLException ex) {
            System.out.println("Error while creating cloudant Client.\nError: " + ex.toString());
            Logger.getLogger(CloudantManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean addToCloudant() {
        try {
            JSONObject jsonString = new JSONObject();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            jsonString.put("key", ++itemsInDb);
            jsonString.put("Time", timestamp);
            jsonString.put("Temperature", temperature);
            jsonString.put("Humidity", humidity);
            jsonString.put("Pressure", pressure);
            jsonString.put("WindDirection", wind_dir);
            jsonString.put("WindSpeed", wind_speed);
            jsonString.put("RainFall", rain_in);
            jsonString.put("Luminosity", light_lvl);
            database.save(jsonString.toMap());
            return true;
        } catch (JSONException ex) {
            System.out.println("Error While Inserting data./nError: " + ex.toString());
        }
        return false;
    }

    private double getItemsInDb() {
        double get = 0.0;
        try {
            String tempQuery = "{ \"selector\": { \"key\": { \"$gt\": " + 0 + " } }, \"sort\": [ { \"key\": \"desc\" } ] }";
            QueryResult<LinkedTreeMap> queryResult = database.query(tempQuery, LinkedTreeMap.class);
            List<LinkedTreeMap> resultList = queryResult.getDocs();
            get = (double) resultList.get(0).get("key");
            System.out.println("Available items in database: " + get);
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return get;
    }

    public void fetchData(int window, String databaseName) {
        double itemInDb = getItemsInDb();
        int indexFrom = (int) (itemInDb - window);

        String tempQuery = "{ \"selector\": { \"key\": { \"$gt\": " + indexFrom + " } }, \"sort\": [ { \"key\": \"desc\" } ] }";
        System.out.println("Query: " + tempQuery);
        QueryResult<LinkedTreeMap> queryResult = database.query(tempQuery, LinkedTreeMap.class);
        List<LinkedTreeMap> resultList = queryResult.getDocs();
        System.out.println("Result List Size: " + resultList.size());

        initialiseArrays(window);
        for (int i = 0; i < resultList.size(); i++) {
            LinkedTreeMap result = resultList.get(i);
            double tmpV = (double) result.get("Temperature");
            double tempWSpeed = (double) result.get("WindSpeed");
            double tempWDirection = (double) result.get("WindDirection");
            double tempRain = (double) result.get("RainFall");
            double tempPresure = (double) result.get("Pressure");
            double tempLuminosity = (double) result.get("Luminosity");
            double tempHumidity = (double) result.get("Humidity");

            temperatureVector[i] = tmpV;
            windSpeedVector[i] = tempWSpeed;
            winDirVector[i] = tempWDirection;
            rainfallVector[i] = tempRain;
            presureVector[i] = tempPresure;
            humidityVector[i] = tempHumidity;
            light_levelVector[i] = tempLuminosity;
        }
    }

    public void initialiseArrays(int window) {
        temperatureVector = new double[window];
        windSpeedVector = new double[window];
        winDirVector = new double[window];
        rainfallVector = new double[window];
        presureVector = new double[window];
        humidityVector = new double[window];
        light_levelVector = new double[window];
    }

    public double[] getWindSpeedVector() {
        return windSpeedVector;
    }

    public double[] getWinDirVector() {
        return winDirVector;
    }

    public double[] getHumidityVector() {
        return humidityVector;
    }

    public double[] getTemperatureVector() {
        return temperatureVector;
    }

    public double[] getRainfallVector() {
        return rainfallVector;
    }

    public double[] getPressureVector() {
        return presureVector;
    }

    public double[] getLight_levelVector() {
        return light_levelVector;
    }

    public void setWind_speed(Double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setWind_dir(Double wind_dir) {
        this.wind_dir = wind_dir;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setRain_in(Double rain_in) {
        this.rain_in = rain_in;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public void setLight_lvl(Double light_lvl) {
        this.light_lvl = light_lvl;
    }

    public double[][] getVariablesFromCloud() {
        double[] temperatureS = getTemperatureVector();
        double[] humidityS = getHumidityVector();
        double[] light_intensityS = getLight_levelVector();
        double[] rainfallS = getRainfallVector();
        double[] atmospheric_pressureS = getPressureVector();
        double[] wind_directionS = getWinDirVector();
        double[] wind_speedS = getWindSpeedVector();
        return new double[][]{temperatureS, humidityS, light_intensityS, rainfallS, atmospheric_pressureS,
                wind_directionS, wind_speedS};
    }
}
