package jb;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import jb.config.Opts;
import jb.data.ClfTuple;
import jb.data.Dataset;
import jb.data.ScoreTuple;
import jb.files.FileHelper;
import jb.files.FileReaderNWay;
import jb.files.FileReaderSameDataset;
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

        Opts opts = new Opts();
        opts.setFilename("/home/jb/Downloads/test0.txt");
        opts.setBias(1);
        opts.setNumberOfBaseClassifiers(3);
        opts.setNumberOfSelectedClassifiers(2);
        opts.setNumberOfSpaceParts(3);
        opts.setSolverType(SolverType.L2R_LR);
        opts.setC(1);
        opts.setEps(.01);
        Dataset dataset = fileHelper.readFile(opts);
        List<Model> clfs = trainer.train(dataset, opts);
        ScoreTuple scoreTuple = validator.validate(clfs, dataset, opts);
        ClfTuple clfTuple = selector.select(clfs, scoreTuple, opts);
        double score = integrator.integrateScore(clfTuple, opts);
        System.out.println(score);

    }
}
