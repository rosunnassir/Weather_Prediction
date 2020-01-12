package ARIMA;

import java.util.Random;
import java.util.Vector;

public class ARIMA1 {

    static boolean infinteloop = false;
    double[] originalData = {};
    ARMAMath armamath = new ARMAMath();
    double stderrDara = 0;
    double avgsumData = 0;
    Vector<double[]> armaARMAcoe = new Vector<>();
    Vector<double[]> bestarmaARMAcoe = new Vector<>();

    public ARIMA1(double[] originalData) {
        this.originalData = originalData;
    }

    public boolean getInfinteLoop() {
        return infinteloop;
    }

    public double[] preDealDif() {

        //seasonal Difference:Peroid=7
        double[] tempData = new double[originalData.length - 7];
        for (int i = 0; i < originalData.length - 7; i++) {
            tempData[i] = originalData[i + 7] - originalData[i];
        }

        return tempData;
    }

    public double[] preDealNor(double[] tempData) {
        //Z-Score
        avgsumData = armamath.avgData(tempData);
        stderrDara = armamath.stderrData(tempData);

        for (int i = 0; i < tempData.length; i++) {
            tempData[i] = (tempData[i] - avgsumData) / stderrDara;
        }

        return tempData;
    }

    public int[] getARIMAmodel() {
        double[] stdoriginalData = this.preDealDif();//ԭʼ���ݲ�ִ���

        int paraType = 0;
        double minAIC = 9999999;
        int bestModelindex = 0;
        int[][] model = new int[][]{{0, 1}, {1, 0}, {1, 1}, {0, 2}, {2, 0}, {2, 2}, {1, 2}, {2, 1}};

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < model.length; i++) {
            if (model[i][0] == 0) {

                MA ma = new MA(stdoriginalData, model[i][1]);
                armaARMAcoe = ma.MAmodel();
                paraType = 1;
            } else if (model[i][1] == 0) {

                AR ar = new AR(stdoriginalData, model[i][0]);
                armaARMAcoe = ar.ARmodel();
                paraType = 2;
            } else {

                ARMA arma = new ARMA(stdoriginalData, model[i][0], model[i][1]);
                armaARMAcoe = arma.ARMAmodel();
                paraType = 3;
            }

            double temp = getmodelAIC(armaARMAcoe, stdoriginalData, paraType);

            if (temp < minAIC) {
                bestModelindex = i;
                minAIC = temp;
                bestarmaARMAcoe = armaARMAcoe;
            }
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;

            if (diffTime > 3000) {
                infinteloop = true;
            }
            if (infinteloop) {
                bestModelindex = 0;
                break;
            }
        }

        int[] model1 = model[bestModelindex];
        return model[bestModelindex];
    }

    public double getmodelAIC(Vector<double[]> para, double[] stdoriginalData, int type) {
        double temp;
        double temp2;
        double sumerr = 0;
        int p = 0;
        int q = 0;
        int n = stdoriginalData.length;
        Random random = new Random();

        switch (type) {
            case 1: {
                double[] maPara = para.get(0);
                q = maPara.length;
                double[] err = new double[q];  //error(t),error(t-1),error(t-2)...
                for (int k = q - 1; k < n; k++) {
                    temp = 0;
                    for (int i = 1; i < q; i++) {
                        temp += maPara[i] * err[i];
                    }
                    for (int j = q - 1; j > 0; j--) {
                        err[j] = err[j - 1];
                    }
                    err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
                    sumerr += (stdoriginalData[k] - (temp)) * (stdoriginalData[k] - (temp));
                }

                return (n - (q - 1)) * Math.log(sumerr / (n - (q - 1))) + (q + 1) * 2;
            }
            case 2: {
                double[] arPara = para.get(0);
                p = arPara.length;
                for (int k = p - 1; k < n; k++) {
                    temp = 0;
                    for (int i = 0; i < p - 1; i++) {
                        temp += arPara[i] * stdoriginalData[k - i - 1];
                    }
                    sumerr += (stdoriginalData[k] - temp) * (stdoriginalData[k] - temp);
                }
                return (n - (q - 1)) * Math.log(sumerr / (n - (q - 1))) + (p + 1) * 2;

            }
            default: {
                double[] arPara = para.get(0);
                double[] maPara = para.get(1);

                p = arPara.length;
                q = maPara.length;
                double[] err = new double[q];

                for (int k = p - 1; k < n; k++) {
                    temp = 0;
                    temp2 = 0;
                    for (int i = 0; i < p - 1; i++) {
                        temp += arPara[i] * stdoriginalData[k - i - 1];
                    }
                    for (int i = 1; i < q; i++) {
                        temp2 += maPara[i] * err[i];
                    }
                    for (int j = q - 1; j > 0; j--) {
                        err[j] = err[j - 1];
                    }

                    err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
                    sumerr += (stdoriginalData[k] - (temp2 + temp)) * (stdoriginalData[k] - (temp2 + temp));
                }
                return (n - (q - 1)) * Math.log(sumerr / (n - (q - 1))) + (p + q) * 2;

            }
        }
    }

    public double aftDeal(int predictValue) {
        return (predictValue + originalData[originalData.length - 7]);
    }

    public int predictValue(int p, int q) {
        int predict = 0;
        double[] stdoriginalData = this.preDealDif();
        int n = stdoriginalData.length;
        double temp = 0, temp2 = 0;
        double[] err = new double[q + 1];

        Random random = new Random();
        if (p == 0) {
            double[] maPara = bestarmaARMAcoe.get(0);

            for (int k = q; k < n; k++) {
                temp = 0;
                for (int i = 1; i <= q; i++) {
                    temp += maPara[i] * err[i];
                }

                for (int j = q; j > 0; j--) {
                    err[j] = err[j - 1];
                }
                err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
            }
            predict = (int) (temp);
        } else if (q == 0) {
            double[] arPara = bestarmaARMAcoe.get(0);

            for (int k = p; k < n; k++) {
                temp = 0;
                for (int i = 0; i < p; i++) {
                    temp += arPara[i] * stdoriginalData[k - i - 1];//yt=ar+ar1(Yt-1)+ar2(Yt-2)...
                }
            }
            predict = (int) (temp);
        } else {
            double[] arPara = bestarmaARMAcoe.get(0);
            double[] maPara = bestarmaARMAcoe.get(1);

            err = new double[q + 1];
            for (int k = p; k < n; k++) {
                temp = 0;
                temp2 = 0;
                for (int i = 0; i < p; i++) {
                    temp += arPara[i] * stdoriginalData[k - i - 1];
                }
                for (int i = 1; i <= q; i++) {
                    temp2 += maPara[i] * err[i];
                }
                for (int j = q; j > 0; j--) {
                    err[j] = err[j - 1];
                }
                err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
            }
            predict = (int) (temp2 + temp);
        }
        return predict;
    }

    public double[] getMApara(double[] autocorData, int q) {
        double[] maPara = new double[q + 1];
        double[] tempmaPara = maPara;
        double temp = 0;
        boolean iterationFlag = true;
        while (iterationFlag) {
            for (int i = 1; i < maPara.length; i++) {
                temp += maPara[i] * maPara[i];
            }
            tempmaPara[0] = autocorData[0] / (1 + temp);
            for (int i = 1; i < maPara.length; i++) {
                temp = 0;
                for (int j = 1; j < maPara.length - i; j++) {
                    temp += maPara[j] * maPara[j + i];
                }
                tempmaPara[i] = -(autocorData[i] / tempmaPara[0] - temp);
            }
            iterationFlag = false;
            for (int i = 0; i < maPara.length; i++) {
                if (maPara[i] != tempmaPara[i]) {
                    iterationFlag = true;
                    break;
                }
            }
            maPara = tempmaPara;
        }
        return maPara;
    }

    public double predict(ARIMA1 arima) {
        int[] model = new int[2];
        try {
            model = arima.getARIMAmodel();
            if (getInfinteLoop()) {
                return 999999;
            }
        } catch (RuntimeException ex) {
//            System.out.println(Arrays.toString(originalData));
//            System.out.println("Error");
        }
        double result = arima.aftDeal(arima.predictValue(model[0], model[1]));
        return (result);
    }
}
