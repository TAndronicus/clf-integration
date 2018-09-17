package jb.tester;

import jb.config.Opts;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.data.Problem;
import jb.data.ValidatingTestingTuple;

import static jb.util.ModelUtils.getIndexOfSubspace;

public class IntegratedScoreTester {

    public double test(IntegratedModel integratedModel, ValidatingTestingTuple validatingTestingTuple, Opts opts) {
        double counter = 0;
        Problem problem = validatingTestingTuple.getTestingProblem();
        for (int i = 0; i < problem.getY().length; i++) {
            int index = getIndexOfSubspace(opts.getNumberOfSpaceParts(), problem.getX()[i][0], validatingTestingTuple.getMinX(), validatingTestingTuple.getMaxX());
            double value = integratedModel.getA()[index] * problem.getX()[i][0] + integratedModel.getB()[index];
            if (value > problem.getX()[i][1] ^ problem.getY()[i] == 1) {
                counter++;
            }
        }
        double score = counter / problem.getY().length;
        score = score > .5 ? score : 1 - score;
        return score;
    }
}
