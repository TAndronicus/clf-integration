package jb.util;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.data.ClfObject;

import java.util.List;

import static jb.config.Constants.EPSILON;

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

    public static boolean predictsPropperly(Model model, ClfObject clfObject) {
        double prediction = Linear.predict(model, clfObject.getX());
        return prediction == clfObject.getY();
    }

    public static int getIndexOfSubspace(int numberOfSpaceParts, double xSample, double minX, double maxX) {
        return (int) (numberOfSpaceParts * (xSample - minX) / (maxX - minX) - EPSILON);
    }

    public static double getScoreFromConfMat(int[][] confMat) {
        return (.0 + confMat[0][0] + confMat[1][1]) / (confMat[0][0] + confMat[0][1] + confMat[1][0] + confMat[1][1]);
    }

    public static void switchConfMatColumns(int[][] confusionMatrix) {
        for (int i = 0; i < confusionMatrix.length; i++) {
            int temp = confusionMatrix[i][0];
            confusionMatrix[i][0] = confusionMatrix[i][1];
            confusionMatrix[i][1] = temp;
        }
    }

}
