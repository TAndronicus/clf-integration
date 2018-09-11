package jb.tester;

import de.bwaldvogel.liblinear.Feature;
import jb.data.Dataset;
import jb.data.IntegratedModel;

import static jb.util.ModelUtils.getIndexOfSubspace;

public class ScoreTester {

    public double test(IntegratedModel integratedModel, Dataset dataset) {
        int counter = 0;
        for (Feature[] features : dataset.getTestingProblem().x) {
            int index = getIndexOfSubspace(integratedModel.getA().length, features[0].getValue(), dataset.getMinX(), dataset.getMaxX());
            double value = integratedModel.getA()[index] * features[0].getValue() + integratedModel.getB()[index];
            if (value > features[1].getValue()) {
                counter++;
            }
        }
        double score = counter / dataset.getTestingProblem().l;
        score = score > .5 ? score : 1 - score;
        return score;
    }
}
