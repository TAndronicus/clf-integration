package jb;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.data.ScoreTuple;
import jb.data.SelectedTuple;
import jb.files.FileHelper;
import jb.files.FileReaderNWay;
import jb.integrator.Integrator;
import jb.integrator.MeanIntegrator;
import jb.selector.NBestSelector;
import jb.selector.Selector;
import jb.tester.IntegratedScoreTester;
import jb.tester.MVScoreTester;
import jb.trainer.SVMTrainer;
import jb.trainer.Trainer;
import jb.validator.SimpleScoreValidator;
import jb.validator.Validator;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MultiRunner {

    static FileHelper fileHelper = new FileReaderNWay();
    static Trainer trainer = new SVMTrainer();
    static Validator validator = new SimpleScoreValidator();
    static Selector selector = new NBestSelector();
    static Integrator integrator = new MeanIntegrator();

    public static void main(String[] args) throws IOException, InvalidInputDataException {

        String rootPath = "src/main/resources/target";
        Opts opts = Opts.builder().bias(1).solverType(SolverType.L2R_LR).C(1).eps(.01).build();
        int[] numbersOfBaseClassifiers = {3, 5, 7, 9};
        int[] numbersOfSpaceParts = {3, 4, 5, 6, 7, 8, 9, 10};
        Date before = new Date();

        for (File file : (new File(rootPath)).listFiles()) {
            for (int numberOfSpaceParts : numbersOfSpaceParts) {
                opts.setNumberOfSpaceParts(numberOfSpaceParts);
                for (int numberOfBaseClassifiers : numbersOfBaseClassifiers) {
                    opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
                    for (int numberOfSelectedClassifiers = 2; numberOfSelectedClassifiers <= numberOfBaseClassifiers; numberOfSelectedClassifiers++) {
                        opts.setNumberOfSelectedClassifiers(numberOfSelectedClassifiers);
                        System.out.println("File: " + file.getName());
                        opts.setFilename(file.getPath());
                        Dataset dataset = fileHelper.readFile(opts);
                        List<Model> clfs = trainer.train(dataset, opts);
                        ScoreTuple scoreTuple = validator.validate(clfs, dataset, opts);
                        SelectedTuple selectedTuple = selector.select(scoreTuple, opts);
                        IntegratedModel integratedModel = integrator.integrate(selectedTuple, clfs, opts);
                        IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();
                        MVScoreTester mvScoreTester = new MVScoreTester();
                        System.out.println(integratedScoreTester.test(integratedModel, dataset));
                        System.out.println(mvScoreTester.test(clfs, dataset));
                    }
                }
            }
        }

        Date after = new Date();

        System.out.println("Took " + ((after.getTime() - before.getTime()) / 1000));

    }
}
