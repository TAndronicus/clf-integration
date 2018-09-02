package jb;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.ClfTuple;
import jb.data.Dataset;
import jb.data.ScoreTuple;
import jb.files.FileHelper;
import jb.files.FileReader10Way;
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

    FileHelper fileHelper = new FileReader10Way();
    Trainer trainer = new SVMTrainer();
    Validator validator = new SimpleScoreValidator();
    Selector selector = new BestSelector();
    Integrator integrator = new MeanIntegrator();

    public static void main(String[] args) throws IOException, InvalidInputDataException {

        Opts opts = new Opts();
        Dataset dataset = fileHelper.readFile("");
        List<Model> clfs = trainer.train(dataset, opts);
        ScoreTuple scoreTuple = validator.validate(clfs, dataset, opts);
        ClfTuple clfTuple = selector.select(clfs, scoreTuple, opts);
        double score = integrator.integrateScore(clfTuple, opts);

    }
}
