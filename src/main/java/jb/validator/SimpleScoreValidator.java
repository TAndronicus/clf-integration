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

        double[] scores = new double[clfs.size()];
        double[] weights = new double[clfs.size()];

        for (int i = 0; i < dataset.getValidatingProblems().size(); i++) {
            double propperlyClassified = 0;
            for (int j = 0; j < dataset.getValidatingProblems().get(i).y.length; j++) {
                double prediction = Linear.predict(clfs.get(i), dataset.getValidatingProblems().get(i).x[j]);
                if(prediction == dataset.getTrainingProblems().get(i).y[j]) {
                    propperlyClassified += 1;
                }
            }
            scores[i] = propperlyClassified / dataset.getValidatingProblems().get(i).y.length;
            weights[i] = scores[i];
        }

        return new ScoreTuple(scores, weights);
    }

}
