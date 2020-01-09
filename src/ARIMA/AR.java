package ARIMA;

import java.util.Vector;

public class AR {

    double[] stdoriginalData = {};
    int p;
    ARMAMath armamath = new ARMAMath();

    public AR(double[] stdoriginalData, int p) {
        this.stdoriginalData = new double[stdoriginalData.length];
        System.arraycopy(stdoriginalData, 0, this.stdoriginalData, 0, stdoriginalData.length);
        this.p = p;
    }

    public Vector<double[]> ARmodel() {
        Vector<double[]> v = new Vector<>();
        v.add(armamath.parcorrCompute(stdoriginalData, p, 0));
        return v;
    }

}
