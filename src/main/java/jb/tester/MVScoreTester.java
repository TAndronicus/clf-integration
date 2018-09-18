package jb.tester;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.data.Problem;
import jb.data.ValidatingTestingTuple;

import java.util.List;

public class MVScoreTester {

    public double test(List<Model> clfs, ValidatingTestingTuple validatingTestingTuple) {
        double score = 0;
        Problem problem = validatingTestingTuple.getTestingProblem();
        for (int i = 0; i < problem.getY().length; i++) {
            double votingResult = 0;
            for (Model model : clfs) {
                votingResult += Linear.predict(model, problem.getObjectAsFeatures(i));
            }
            if (votingResult / clfs.size() > .5 ^ problem.getY()[i] == 1) {
                score++;
            }
        }
        score /= problem.getY().length;
        return score > .5 ? score : 1 - score;
    }
}
