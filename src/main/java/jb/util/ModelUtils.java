package jb.util;

import de.bwaldvogel.liblinear.Model;

import java.util.List;

public class ModelUtils {

    public static double[] getAs(List<Model> clfs) {
        double[] as = new double[clfs.size()];
        for (int i = 0; i < clfs.size(); i++) {
            as[i] = clfs.get(i).getFeatureWeights()[0] / clfs.get(i).getFeatureWeights()[1];
        }
        return as;
    }

    public static double[] getBs(List<Model> clfs) {
        return new double[clfs.size()];
    }

}
