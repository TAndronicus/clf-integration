package jb.tester;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.data.Dataset;
import jb.data.Problem;
import jb.util.ModelUtils;

import java.util.List;

public class MVConfMatTester {

    public int[][] test(List<Model> clfs, Problem problem) {
        int[][] confMat = {{0, 0}, {0, 0}};
        int realIndex;
        int predictedIndex;
        for (int i = 0; i < problem.getY().length; i++) {
            realIndex = problem.getY()[i] == 1 ? 1 : 0;
            double votingResult = 0;
            for (Model model : clfs) {
//                votingResult += Linear.predict(model, problem.convertToLibLinearProblem().x[i]);
            }
            predictedIndex = votingResult / clfs.size() > .5 ? 1 : 0;
            confMat[realIndex][predictedIndex]++;
        }
        if (ModelUtils.getScoreFromConfMat(confMat) < .5) ModelUtils.switchConfMatColumns(confMat);
        return confMat;
    }

}
