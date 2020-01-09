package Misc;

import ChartBuilder.LineChart_AWT;
import FileUtils.CSVReader;
import org.jfree.ui.RefineryUtilities;

public class AlgorithmTest {
    public static String algorithm = "MLR_Single_File";
    public DatabaseAnalyser databaseAnalyser;

    public static void main(String[] args) throws InterruptedException {

        double[][] error = new double[3][7];

        for (int i = 20; i <= 60; i += 20) {

            System.out.println("Error for prediction Time : " + i);
            System.out.println("Creating Tester Algorithm");
            AlgorithmTest testerAlgo1 = new AlgorithmTest();
            AlgorithmTest testerAlgo2 = new AlgorithmTest();
            AlgorithmTest testerAlgo3 = new AlgorithmTest();
            AlgorithmTest testerAlgo4 = new AlgorithmTest();
            AlgorithmTest testerAlgo5 = new AlgorithmTest();
            AlgorithmTest testerAlgo6 = new AlgorithmTest();
            AlgorithmTest testerAlgo7 = new AlgorithmTest();
            AlgorithmTest testerAlgo8 = new AlgorithmTest();
            AlgorithmTest testerAlgo9 = new AlgorithmTest();
            AlgorithmTest testerAlgo10 = new AlgorithmTest();
            System.out.println("Done");

            System.out.println("Analysing Data....");
            testerAlgo1.analyser(1, 2000, i);
            testerAlgo2.analyser(2000, 4000, i);
            testerAlgo3.analyser(4000, 6000, i);
            testerAlgo4.analyser(6000, 8000, i);
            testerAlgo5.analyser(8000, 10000, i);
            testerAlgo6.analyser(10000, 12000, i);
            testerAlgo7.analyser(12000, 14000, i);
            testerAlgo8.analyser(14000, 16000, i);
            testerAlgo9.analyser(16000, 18000, i);
            testerAlgo10.analyser(18000, 20000, i);
            System.out.println("Joining Thread");

            testerAlgo1.databaseAnalyser.join();
            testerAlgo2.databaseAnalyser.join();
            testerAlgo3.databaseAnalyser.join();
            testerAlgo4.databaseAnalyser.join();
            testerAlgo5.databaseAnalyser.join();
            testerAlgo6.databaseAnalyser.join();
            testerAlgo7.databaseAnalyser.join();
            testerAlgo8.databaseAnalyser.join();
            testerAlgo9.databaseAnalyser.join();
            testerAlgo10.databaseAnalyser.join();

            double[] error1 = testerAlgo1.databaseAnalyser.getError();
            double[] error2 = testerAlgo2.databaseAnalyser.getError();
            double[] error3 = testerAlgo3.databaseAnalyser.getError();
            double[] error4 = testerAlgo4.databaseAnalyser.getError();
            double[] error5 = testerAlgo5.databaseAnalyser.getError();
            double[] error6 = testerAlgo6.databaseAnalyser.getError();
            double[] error7 = testerAlgo7.databaseAnalyser.getError();
            double[] error8 = testerAlgo8.databaseAnalyser.getError();
            double[] error9 = testerAlgo9.databaseAnalyser.getError();
            double[] error10 = testerAlgo10.databaseAnalyser.getError();

            for (int j = 0; j < 7; j++) {
                double temp1 = error1[j];
                double temp2 = error2[j];
                double temp3 = error3[j];
                double temp4 = error4[j];
                double temp5 = error5[j];
                double temp6 = error6[j];
                double temp7 = error7[j];
                double temp8 = error8[j];
                double temp9 = error9[j];
                double temp10 = error10[j];

                final double v = temp1 + temp2 + temp3 + temp4 + temp5 + temp6 + temp7 + temp8 + temp9 + temp10;
                switch (i) {
                    case 20:
                        error[0][j] = v / 10;
                        break;
                    case 40:
                        error[1][j] = v / 10;
                        break;
                    case 60:
                        error[2][j] = v / 10;
                        break;
                }
            }

        }

        LineChart_AWT ch = new LineChart_AWT("Percentage Error Graph", "Percentage Error for "
                + algorithm, error);
        ch.pack();
        RefineryUtilities.centerFrameOnScreen(ch);
        ch.setVisible(true);

    }

    private void analyser(int startIndex, int endIndex, int ptime) {
        CSVReader firstReader = new CSVReader("Book1.csv", startIndex, endIndex);
        CSVReader secondReader = new CSVReader("Book2.csv", startIndex, endIndex);
        final double[][] variables_Database_1 = getVariablesFromReader(firstReader);
        final double[][] variables_Database_2 = getVariablesFromReader(secondReader);
        databaseAnalyser = new DatabaseAnalyser(algorithm, 200, variables_Database_1,
                variables_Database_2, ptime);
        databaseAnalyser.start();
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
