package ARIMA;

import java.util.Vector;

public class MA {

    double[] stdoriginalData = {};
    int q;
    ARMAMath armamath = new ARMAMath();

    public MA(double[] stdoriginalData, int q) {
        this.stdoriginalData = new double[stdoriginalData.length];
        System.arraycopy(stdoriginalData, 0, this.stdoriginalData, 0, stdoriginalData.length);
        this.q = q;

    }

    public Vector<double[]> MAmodel() {
        Vector<double[]> v = new Vector<>();
        v.add(armamath.getMApara(armamath.autocorGrma(stdoriginalData, q), q));
        return v;
    }

}
