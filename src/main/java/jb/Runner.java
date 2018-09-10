package jb;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.ScoreTuple;
import jb.data.SelectedTuple;
import jb.files.FileHelper;
import jb.files.FileReaderNWay;
import jb.integrator.Integrator;
import jb.integrator.MeanIntegrator;
import jb.selector.BestSelector;
import jb.selector.Selector;
import jb.trainer.SVMTrainer;
import jb.trainer.Trainer;
import jb.validator.SimpleScoreValidator;
import jb.validator.Validator;

import java.io.IOException;
import java.util.List;

public class Runner {

    static FileHelper fileHelper = new FileReaderNWay();
    static Trainer trainer = new SVMTrainer();
    static Validator validator = new SimpleScoreValidator();
    static Selector selector = new BestSelector();
    static Integrator integrator = new MeanIntegrator();

    public static void main(String[] args) throws IOException, InvalidInputDataException {

        Opts opts = Opts.builder().filename("/home/jb/Downloads/data4.txt").bias(1).numberOfBaseClassifiers(3).numberOfSelectedClassifiers(2).
                numberOfSpaceParts(3).solverType(SolverType.L2R_LR).C(1).eps(.01).build();
        Dataset dataset = fileHelper.readFile(opts);
        List<Model> clfs = trainer.train(dataset, opts);
        ScoreTuple scoreTuple = validator.validate(clfs, dataset, opts);
        SelectedTuple selectedTuple = selector.select(scoreTuple, opts);
        double score = integrator.integrateScore(selectedTuple, opts);
        System.out.println(score);

    }
}
