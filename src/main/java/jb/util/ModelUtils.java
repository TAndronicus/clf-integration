package jb.util;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.clfobj.ClfObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jb.config.Constants.EPSILON;

public class ModelUtils {

    public static double[] getAs(List<Model> clfs) {
        double[] as = new double[clfs.size()];
        for (int i = 0; i < clfs.size(); i++) {
            as[i] = -clfs.get(i).getFeatureWeights()[0] / clfs.get(i).getFeatureWeights()[1];
        }
        return as;
    }

    public static double[] getBs(List<Model> clfs) {
        double[] bs = new double[clfs.size()];
        for (int i = 0; i < clfs.size(); i++) {
            bs[i] = -clfs.get(i).getFeatureWeights()[2] / clfs.get(i).getFeatureWeights()[1];
        }
        return bs;
    }

    public static boolean predictsPropperly(Model model, ClfObject clfObject) {
        Feature[] features = new Feature[clfObject.getX().length];
        for (int i = 0; i < clfObject.getX().length; i++) {
            features[i] = new FeatureNode(i + 1, clfObject.getX()[i]);
        }
        double prediction = Linear.predict(model, features);
        return prediction == clfObject.getY();
    }

    public static int getIndexOfSubspace(int numberOfSpaceParts, double xSample, double minX, double maxX) {
        return (int) (numberOfSpaceParts * (xSample - minX) / (maxX - minX) - EPSILON);
    }

    public static int getIndexOfSubspace(double[] x, double sample) {
        int ind = Arrays.binarySearch(x, sample);
        ind = ind < 0 ? -2 - ind : ind - 1;
        ind = ind == -1 ? 0 : ind;
        return ind;
    }

    public static double calculateScoreFromConfMat(int[][] confMat) {
        return (.0 + confMat[0][0] + confMat[1][1]) / (confMat[0][0] + confMat[0][1] + confMat[1][0] + confMat[1][1]);
    }

    public static void switchConfMatColumns(int[][] confusionMatrix) {
        for (int i = 0; i < confusionMatrix.length; i++) {
            int temp = confusionMatrix[i][0];
            confusionMatrix[i][0] = confusionMatrix[i][1];
            confusionMatrix[i][1] = temp;
        }
    }

    public static double calculateMccFromConfMat(int[][] confMat) {
        double nominator = .0 + confMat[0][0] * confMat[1][1] - confMat[0][1] * confMat[1][0];
        double denominatorSq = 1.0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                denominatorSq *= (confMat[i][i] + confMat[j][1 - j]);
            }
        }
        double denominator = Math.sqrt(denominatorSq);
        return denominator == 0 ? 0 : nominator / denominator;
    }

    public static double calculateRelativeValue(double absoluteValue, double minValue, double maxValue) {
        return (absoluteValue - minValue) / (maxValue - minValue);
    }

    public static List<Model> pickModels(List<Model> clfs, Opts opts) {
        List<Model> pickedModels = new ArrayList<>();
        for (int i = 0; i < clfs.size(); i++)
            if (i != opts.getPermutation()[0] && i != opts.getPermutation()[1]) pickedModels.add(clfs.get(i));
        return pickedModels;
    }

    public static List<Model> pickModels(List<Model> clfs, int[] indices) {
        List<Model> selectedClfs = new ArrayList<>();
        for (int index : indices) {
            selectedClfs.add(clfs.get(index));
        }
        return selectedClfs;
    }

}
