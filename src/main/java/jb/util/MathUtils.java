package jb.util;

import java.util.ArrayList;
import java.util.List;

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

    public static List<int[]> getCombinationsOfTwo(int range) {
        List<int[]> combinations = new ArrayList<>();
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                if (i != j) combinations.add(new int[]{i, j});
            }
        }
        return combinations;
    }

}
