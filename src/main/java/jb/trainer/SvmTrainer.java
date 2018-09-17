package jb.trainer;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import jb.config.Opts;
import jb.data.Dataset;

import java.util.ArrayList;
import java.util.List;

public class SvmTrainer implements Trainer{

    @Override
    public List<Model> train(Dataset dataset, Opts opts) {
        Parameter parameter = new Parameter(opts.getSolverType(), opts.getC(), opts.getEps());
        List<Model> clfs = new ArrayList<>();
        for (jb.data.Problem problem : dataset.getProblems()) {
            clfs.add(Linear.train(problem.convertToLibLinearProblem(opts), parameter));
        }
        return clfs;
    }

}
