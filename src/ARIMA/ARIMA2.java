package ARIMA;

import java.util.Random;
import java.util.Vector;

public class ARIMA2 {

    static boolean infinteloop = false;
    double[] originalData = {};
    ARMAMath armamath = new ARMAMath();
    double stderrDara = 0;
    double avgsumData = 0;
    Vector<double[]> armaARMAcoe = new Vector<>();
    Vector<double[]> bestarmaARMAcoe = new Vector<>();
    double[] originalData2 = {};
    ARMAMath armamath2 = new ARMAMath();
    double stderrDara2 = 0;
    double avgsumData2 = 0;
    Vector<double[]> armaARMAcoe2 = new Vector<>();
    Vector<double[]> bestarmaARMAcoe2 = new Vector<>();
    int[] predictarray = new int[2];

    public ARIMA2(double[] originalData, double[] originalData2) {
        this.originalData = originalData;
        this.originalData2 = originalData2;
    }

    public boolean getInfinteLoop() {
        return infinteloop;
    }

    public double[] preDealDif() {
        double[] tempData = new double[originalData.length - 7];
        for (int i = 0; i < originalData.length - 7; i++) {
            tempData[i] = originalData[i + 7] - originalData[i];
        }
        return tempData;
    }

    public double[] preDealDif2() {
        double[] tempData = new double[originalData2.length - 7];
        for (int i = 0; i < originalData2.length - 7; i++) {
            tempData[i] = originalData2[i + 7] - originalData2[i];
        }

        return tempData;
    }

    public double[] preDealNor(double[] tempData) {
        avgsumData = armamath.avgData(tempData);
        stderrDara = armamath.stderrData(tempData);
        for (int i = 0; i < tempData.length; i++) {
            tempData[i] = (tempData[i] - avgsumData) / stderrDara;
        }
        return tempData;
    }

    public double[] preDealNor2(double[] tempData) {
        avgsumData2 = armamath2.avgData(tempData);
        stderrDara2 = armamath2.stderrData(tempData);
        for (int i = 0; i < tempData.length; i++) {
            tempData[i] = (tempData[i] - avgsumData2) / stderrDara2;
        }
        return tempData;
    }

    public int[] getARIMAmodel() {
        double[] stdoriginalData = this.preDealDif();
        int paraType;
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

            if (diffTime > 1000) {
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

    public int[] getARIMAmodel2() {
        double[] stdoriginalData = this.preDealDif2();

        int paraType = 0;
        double minAIC = 9999999;
        int bestModelindex = 0;
        int[][] model = new int[][]{{0, 1}, {1, 0}, {1, 1}, {0, 2}, {2, 0}, {2, 2}, {1, 2}, {2, 1}};

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < model.length; i++) {
            if (model[i][0] == 0) {
                MA ma = new MA(stdoriginalData, model[i][1]);
                armaARMAcoe2 = ma.MAmodel();
                paraType = 1;
            } else if (model[i][1] == 0) {
                AR ar = new AR(stdoriginalData, model[i][0]);
                armaARMAcoe2 = ar.ARmodel();
                paraType = 2;
            } else {
                ARMA arma = new ARMA(stdoriginalData, model[i][0], model[i][1]);
                armaARMAcoe2 = arma.ARMAmodel();
                paraType = 3;
            }

            double temp = getmodelAIC(armaARMAcoe2, stdoriginalData, paraType);
            if (temp < minAIC) {
                bestModelindex = i;
                minAIC = temp;
                bestarmaARMAcoe2 = armaARMAcoe2;
            }
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;
            if (diffTime > 1000) {
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
        double temp = 0;
        double temp2 = 0;
        double sumerr = 0;
        int p = 0;//ar1,ar2,...,sig2
        int q = 0;//sig2,ma1,ma2...
        int n = stdoriginalData.length;
        Random random = new Random();

        if (type == 1) {
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
        } else if (type == 2) {
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

        } else {
            double[] arPara = para.get(0);
            double[] maPara = para.get(1);

            p = arPara.length;
            q = maPara.length;
            double[] err = new double[q];  //error(t),error(t-1),error(t-2)...

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

    public int aftDeal(int predictValue, int predictValue2) {
        return (int) (((predictValue + predictValue2) / 2) + originalData[originalData.length - 7]);
    }

    public int aftDeal2(int predictValue, int predictValue2) {
        return (int) (((predictValue + predictValue2) / 2) + originalData2[originalData2.length - 7]);
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
                    temp += arPara[i] * stdoriginalData[k - i - 1];
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
        predictarray[0] = predict;

        return predict;
    }

    public int predictValue2(int p, int q) {
        int predict = 0;
        double[] stdoriginalData = this.preDealDif2();
        int n = stdoriginalData.length;
        double temp = 0, temp2 = 0;
        double[] err = new double[q + 1];

        Random random = new Random();
        if (p == 0) {
            double[] maPara = bestarmaARMAcoe2.get(0);

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
            double[] arPara = bestarmaARMAcoe2.get(0);

            for (int k = p; k < n; k++) {
                temp = 0;
                for (int i = 0; i < p; i++) {
                    temp += arPara[i] * stdoriginalData[k - i - 1];//yt=ar+ar1(Yt-1)+ar2(Yt-2)...
                }
            }
            predict = (int) (temp);
        } else {
            double[] arPara = bestarmaARMAcoe2.get(0);
            double[] maPara = bestarmaARMAcoe2.get(1);

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
        predictarray[1] = predict;
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

    public double predict(ARIMA2 arima) {
        int[] model = arima.getARIMAmodel();
        int[] model2 = arima.getARIMAmodel2();
        if (getInfinteLoop()) {
            return 999999;
        }
        float result = arima.aftDeal(arima.predictValue(model[0], model[1]), arima.predictValue2(model2[0], model2[1]));
        return (result);
    }

    public double predict2(ARIMA2 arima) {
        int[] model = arima.getARIMAmodel();
        int[] model2 = arima.getARIMAmodel2();

        if (getInfinteLoop()) {
            return 999999;

        }
        float result = arima.aftDeal2(arima.predictValue(model[0], model[1]), arima.predictValue2(model2[0], model2[1]));
        return (result);
    }
}
