package FileUtils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class CSVReader {
    private final static Logger LOGGER = Logger.getLogger(CSVReader.class.getName());
    String path;
    List<Double> T = new ArrayList<>();
    List<Double> WS = new ArrayList<>();
    List<Double> WD = new ArrayList<>();
    List<Double> R = new ArrayList<>();
    List<Double> P = new ArrayList<>();
    List<Double> L = new ArrayList<>();
    List<Double> H = new ArrayList<>();
    double[] temperature;
    double[] windSpeed;
    double[] windDirection;
    double[] rain;
    double[] pressure;
    double[] luminosity;
    double[] humidity;

    public CSVReader(String path, int startIndex, int endIndex) {
        this.path = path;
        try {
            FileReader file = new FileReader(path);
            try (CSVParser parser = new CSVParser(file, CSVFormat.DEFAULT)) {
                List<CSVRecord> list = parser.getRecords();
                for (int i = startIndex; i < endIndex; i++) {
                    CSVRecord record = list.get(i);
                    T.add(Double.parseDouble(record.get(1)));
                    WS.add(Double.parseDouble(record.get(2)));
                    WD.add(Double.parseDouble(record.get(3)));
                    R.add(Double.parseDouble(record.get(4)));
                    P.add(Double.parseDouble(record.get(5)));
                    L.add(Double.parseDouble(record.get(6)));
                    H.add(Double.parseDouble(record.get(7)));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        Double[] T1 = new Double[T.size()];
        Double[] WS1 = new Double[T.size()];
        Double[] WD1 = new Double[T.size()];
        Double[] R1 = new Double[T.size()];
        Double[] P1 = new Double[T.size()];
        Double[] L1 = new Double[T.size()];
        Double[] H1 = new Double[T.size()];

        T1 = T.toArray(T1);
        WS1 = WS.toArray(WS1);
        WD1 = WD.toArray(WD1);
        R1 = R.toArray(R1);
        P1 = P.toArray(P1);
        L1 = L.toArray(L1);
        H1 = H.toArray(H1);

        temperature = Stream.of(T1).mapToDouble(Double::doubleValue).toArray();
        windSpeed = Stream.of(WS1).mapToDouble(Double::doubleValue).toArray();
        windDirection = Stream.of(WD1).mapToDouble(Double::doubleValue).toArray();
        rain = Stream.of(R1).mapToDouble(Double::doubleValue).toArray();
        pressure = Stream.of(P1).mapToDouble(Double::doubleValue).toArray();
        luminosity = Stream.of(L1).mapToDouble(Double::doubleValue).toArray();
        humidity = Stream.of(H1).mapToDouble(Double::doubleValue).toArray();

    }

    public CSVReader(String path, int window, boolean cloudant) {
        this.path = path;
        int index = 0;
        try {

            FileReader file = new FileReader(path);
            try (CSVParser parser = new CSVParser(file, CSVFormat.DEFAULT)) {
                List<CSVRecord> list = parser.getRecords();
                if (cloudant) {
                    index = 1;
                } else {
                    index = list.size() - window;
                }
                for (int i = index; i < list.size(); i++) {
                    CSVRecord record = list.get(i);
                    T.add(Double.parseDouble(record.get(1)));
                    WS.add(Double.parseDouble(record.get(2)));
                    WD.add(Double.parseDouble(record.get(3)));
                    R.add(Double.parseDouble(record.get(4)));
                    P.add(Double.parseDouble(record.get(5)));
                    L.add(Double.parseDouble(record.get(6)));
                    H.add(Double.parseDouble(record.get(7)));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        Double[] T1 = new Double[T.size()];
        Double[] WS1 = new Double[T.size()];
        Double[] WD1 = new Double[T.size()];
        Double[] R1 = new Double[T.size()];
        Double[] P1 = new Double[T.size()];
        Double[] L1 = new Double[T.size()];
        Double[] H1 = new Double[T.size()];

        T1 = T.toArray(T1);
        WS1 = WS.toArray(WS1);
        WD1 = WD.toArray(WD1);
        R1 = R.toArray(R1);
        P1 = P.toArray(P1);
        L1 = L.toArray(L1);
        H1 = H.toArray(H1);

        temperature = Stream.of(T1).mapToDouble(Double::doubleValue).toArray();
        windSpeed = Stream.of(WS1).mapToDouble(Double::doubleValue).toArray();
        windDirection = Stream.of(WD1).mapToDouble(Double::doubleValue).toArray();
        rain = Stream.of(R1).mapToDouble(Double::doubleValue).toArray();
        pressure = Stream.of(P1).mapToDouble(Double::doubleValue).toArray();
        luminosity = Stream.of(L1).mapToDouble(Double::doubleValue).toArray();
        humidity = Stream.of(H1).mapToDouble(Double::doubleValue).toArray();

    }

    public CSVReader(InputStream stream, int window) {
        try {
            BufferedReader file = new BufferedReader(new InputStreamReader(stream));
            try (CSVParser parser = new CSVParser(file, CSVFormat.DEFAULT)) {
                List<CSVRecord> list = parser.getRecords();
                int csvLength = list.size();
                System.out.println("CSV Length: " + csvLength);
                int startIndex = csvLength - window - 1;
                System.out.println("Start Index: " + startIndex);
                for (int i = startIndex; i < csvLength - 1; i++) {
                    CSVRecord record = list.get(i);
                    T.add(Double.parseDouble(record.get(1)));
                    WS.add(Double.parseDouble(record.get(2)));
                    WD.add(Double.parseDouble(record.get(3)));
                    R.add(Double.parseDouble(record.get(4)));
                    P.add(Double.parseDouble(record.get(5)));
                    L.add(Double.parseDouble(record.get(6)));
                    H.add(Double.parseDouble(record.get(7)));
                }
            }
            System.out.println("Fetched : " + T.size() + " values.");
        } catch (IOException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        Double[] T1 = new Double[T.size()];
        Double[] WS1 = new Double[T.size()];
        Double[] WD1 = new Double[T.size()];
        Double[] R1 = new Double[T.size()];
        Double[] P1 = new Double[T.size()];
        Double[] L1 = new Double[T.size()];
        Double[] H1 = new Double[T.size()];

        T1 = T.toArray(T1);
        WS1 = WS.toArray(WS1);
        WD1 = WD.toArray(WD1);
        R1 = R.toArray(R1);
        P1 = P.toArray(P1);
        L1 = L.toArray(L1);
        H1 = H.toArray(H1);

        temperature = Stream.of(T1).mapToDouble(Double::doubleValue).toArray();
        windSpeed = Stream.of(WS1).mapToDouble(Double::doubleValue).toArray();
        windDirection = Stream.of(WD1).mapToDouble(Double::doubleValue).toArray();
        rain = Stream.of(R1).mapToDouble(Double::doubleValue).toArray();
        pressure = Stream.of(P1).mapToDouble(Double::doubleValue).toArray();
        luminosity = Stream.of(L1).mapToDouble(Double::doubleValue).toArray();
        humidity = Stream.of(H1).mapToDouble(Double::doubleValue).toArray();

    }

    public double[] getTemp() {
        return temperature;
    }

    public double[] getWindSpeed() {
        return windSpeed;
    }

    public double[] getWindDirection() {
        return windDirection;
    }

    public double[] getRainfall() {
        return rain;
    }

    public double[] getPressure() {
        return pressure;
    }

    public double[] getLight() {
        return luminosity;
    }

    public double[] getHumidity() {
        return humidity;
    }

}
