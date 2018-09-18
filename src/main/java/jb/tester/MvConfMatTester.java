package jb.tester;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Problem;
import jb.data.ValidatingTestingTuple;
import jb.util.ModelUtils;

import java.util.List;

public class MvConfMatTester {

    public int[][] test(List<Model> clfs, ValidatingTestingTuple validatingTestingTuple, Opts opts) {
        int[][] confMat = {{0, 0}, {0, 0}};
        int realIndex;
        int predictedIndex;
        Problem problem = validatingTestingTuple.getTestingProblem();
        for (int i = 0; i < problem.getY().length; i++) {
            realIndex = problem.getY()[i] == 1 ? 1 : 0;
            double votingResult = 0;
            for (Model model : clfs) {
                votingResult += Linear.predict(model, problem.getObjectAsFeatures(i));
            }
            predictedIndex = votingResult / clfs.size() > .5 ? 1 : 0;
            confMat[realIndex][predictedIndex]++;
        }
        if (ModelUtils.calculateScoreFromConfMat(confMat) < .5) ModelUtils.switchConfMatColumns(confMat);
        return confMat;
    }

}
