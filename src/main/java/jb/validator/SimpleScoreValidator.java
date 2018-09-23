package jb.validator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Problem;
import jb.data.ScoreTuple;
import jb.data.ValidatingTestingTuple;
import jb.data.clfobj.ClfObjectOnceSorted;

import java.util.List;

import static jb.util.ModelUtils.predictsPropperly;


public class SimpleScoreValidator implements Validator {

    @Override
    public ScoreTuple validate(List<? extends Model> clfs, ValidatingTestingTuple validatingTestingTuple, Opts opts) {

        double[][] scores = new double[clfs.size()][opts.getNumberOfSpaceParts()];
        double[][] weights = new double[opts.getNumberOfSpaceParts()][clfs.size()];


        for (int i = 0; i < clfs.size(); i++) {
            for (int j = 0; j < opts.getNumberOfSpaceParts(); j++) {
                double propperlyClassified = 0;
                Problem currentProblem = validatingTestingTuple.getValidationProblems().get(j);
                for (int k = 0; k < currentProblem.getX().length; k++) {
                    if (predictsPropperly(clfs.get(i), new ClfObjectOnceSorted(currentProblem.getX()[k], currentProblem.getY()[k]))) {
                        propperlyClassified++;
                    }
                }
                scores[i][j] = propperlyClassified / validatingTestingTuple.getValidationProblems().get(j).getY().length;
                weights[j][i] = propperlyClassified / validatingTestingTuple.getValidationProblems().get(j).getY().length;
            }
        }

        return new ScoreTuple(scores, weights);
    }

}
