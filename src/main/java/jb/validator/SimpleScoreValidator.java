package jb.validator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.ClfObjectOnceSorted;
import jb.data.Dataset;
import jb.data.ScoreTuple;

import java.util.List;

import static jb.util.ModelUtils.predictsPropperly;

public class SimpleScoreValidator implements Validator {

    @Override
    public ScoreTuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts) {

        double[][] scores = new double[clfs.size()][opts.getNumberOfSpaceParts()];
        double[][] weights = new double[opts.getNumberOfSpaceParts()][clfs.size()];


        /*for (int i = 0; i < clfs.size(); i++) {
            for (int j = 0; j < opts.getNumberOfSpaceParts(); j++) {
                double propperlyClassified = 0;
                for (int k = 0; k < dataset.getValidatingProblems().get(j).l; k++) {
                    if (predictsPropperly(clfs.get(i), new ClfObjectOnceSorted(dataset.getValidatingProblems().get(j).x[k], dataset.getValidatingProblems().get(j).y[k]))) {
                        propperlyClassified += 1;
                    }
                }
                scores[i][j] = propperlyClassified / dataset.getValidatingProblems().get(j).l;
                weights[j][i] = propperlyClassified / dataset.getValidatingProblems().get(j).l;
            }
        }

        return new ScoreTuple(scores, weights);*/
        return null;
    }

}
