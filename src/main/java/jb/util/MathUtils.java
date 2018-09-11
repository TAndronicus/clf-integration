package jb.util;

public class MathUtils {

    public static double vectorProduct(double[] v1, double[] v2) {
        double product = 0;
        for (int i = 0; i < v1.length; i++) {
            product += v1[i] * v2[i];
        }
        return product;
    }

    public static double vectorTrace(double[] v1) {
        double trace = 0;
        for (double v : v1) {
            trace += v;
        }
        return trace;
    }

}
