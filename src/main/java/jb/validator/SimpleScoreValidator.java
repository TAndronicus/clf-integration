package jb.validator;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.ScoreTuple;

import java.util.List;

public class SimpleScoreValidator implements Validator {

    @Override
    public ScoreTuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts) {

        double[][] scores = new double[clfs.size()][opts.getNumberOfSpaceParts()];
        double[][] weights = new double[opts.getNumberOfSpaceParts()][clfs.size()];


        for (int i = 0; i < clfs.size(); i++) {
            for (int j = 0; j < opts.getNumberOfSpaceParts(); j++) {
                double propperlyClassified = 0;
                for (int k = 0; k < dataset.getValidatingProblems().get(j).l; k++) {
                    double prediction = Linear.predict(clfs.get(i), dataset.getValidatingProblems().get(j).x[k]);
                    if (prediction == dataset.getValidatingProblems().get(j).y[k]) {
                        propperlyClassified += 1;
                    }
                }
                scores[i][j] = propperlyClassified / dataset.getValidatingProblems().get(j).l;
                weights[j][i] = propperlyClassified / dataset.getValidatingProblems().get(j).l;
            }
        }

        return new ScoreTuple(scores, weights);
    }

}
