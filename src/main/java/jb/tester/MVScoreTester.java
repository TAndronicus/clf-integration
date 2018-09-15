package jb.tester;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.data.Dataset;

import java.util.List;

public class MVScoreTester {

    public double test(List<Model> clfs, Dataset dataset) {
        double score = 0;
        for (int i = 0; i < dataset.getTestingProblem().l; i++) {
            double votingResult = 0;
            for (Model model : clfs) {
                votingResult += Linear.predict(model, dataset.getTestingProblem().x[i]);
            }
            if (votingResult / clfs.size() > .5 ^ dataset.getTestingProblem().y[i] == 1) {
                score++;
            }
        }
        score /= dataset.getTestingProblem().l;
        return score > .5 ? score : 1 - score;
    }
}
