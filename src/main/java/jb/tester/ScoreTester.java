package jb.tester;

import de.bwaldvogel.liblinear.Feature;
import jb.data.Dataset;
import jb.data.IntegratedModel;

import static jb.util.ModelUtils.getIndexOfSubspace;

public class ScoreTester {

    public double test(IntegratedModel integratedModel, Dataset dataset) {
        double counter = 0;
        for (int i = 0; i < dataset.getTestingProblem().l; i++) {
            int index = getIndexOfSubspace(integratedModel.getA().length, dataset.getTestingProblem().x[i][0].getValue(), dataset.getMinX(), dataset.getMaxX());
            double value = integratedModel.getA()[index] * dataset.getTestingProblem().x[i][0].getValue() + integratedModel.getB()[index];
            if (value > dataset.getTestingProblem().x[i][1].getValue() ^ dataset.getTestingProblem().y[i] == 1) {
                counter++;
            }
        }
        double score = counter / dataset.getTestingProblem().l;
        score = score > .5 ? score : 1 - score;
        return score;
    }
}
