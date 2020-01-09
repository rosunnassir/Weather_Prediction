package ARIMA;

import java.util.Vector;

public class ARMA {

    double[] stdoriginalData = {};
    int p;
    int q;
    ARMAMath armamath = new ARMAMath();

    public ARMA(double[] stdoriginalData, int p, int q) {
        this.stdoriginalData = new double[stdoriginalData.length];
        System.arraycopy(stdoriginalData, 0, this.stdoriginalData, 0, stdoriginalData.length);
        this.p = p;
        this.q = q;
    }

    public Vector<double[]> ARMAmodel() {
        double[] arcoe = armamath.parcorrCompute(stdoriginalData, p, q);
        double[] autocorData = getautocorofMA(p, q, stdoriginalData, arcoe);
        double[] macoe = armamath.getMApara(autocorData, q);
        Vector<double[]> v = new Vector<>();
        v.add(arcoe);
        v.add(macoe);
        return v;
    }

    public double[] getautocorofMA(int p, int q, double[] stdoriginalData, double[] autoRegress) {
        int temp;
        double[] errArray = new double[stdoriginalData.length - p];
        int count = 0;
        for (int i = p; i < stdoriginalData.length; i++) {
            temp = 0;
            for (int j = 1; j <= p; j++) {
                temp += stdoriginalData[i - j] * autoRegress[j - 1];
            }
            errArray[count++] = stdoriginalData[i] - temp;
        }
        return armamath.autocorGrma(errArray, q);
    }
}
