package jb.tester;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.data.Dataset;
import jb.util.ModelUtils;

import java.util.List;

public class MVConfMatTester {

    public int[][] test(List<Model> clfs, Dataset dataset) {
        int[][] confMat = {{0, 0}, {0, 0}};
        int realIndex;
        int predictedIndex;
        for (int i = 0; i < dataset.getTestingProblem().l; i++) {
            realIndex = dataset.getTestingProblem().y[i] == 1 ? 1 : 0;
            double votingResult = 0;
            for (Model model : clfs) {
                votingResult += Linear.predict(model, dataset.getTestingProblem().x[i]);
            }
            predictedIndex = votingResult / clfs.size() > .5 ? 1 : 0;
            confMat[realIndex][predictedIndex]++;
        }
        if (ModelUtils.getScoreFromConfMat(confMat) < .5) ModelUtils.switchConfMatColumns(confMat);
        return confMat;
    }

}
