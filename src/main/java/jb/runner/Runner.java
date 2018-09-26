package jb.runner;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import jb.config.Opts;
import jb.data.*;
import jb.files.BReader;
import jb.files.FileHelper;
import jb.integrator.Integrator;
import jb.integrator.MeanIntegrator;
import jb.selector.NBestSelector;
import jb.selector.Selector;
import jb.tester.IntegratedScoreTester;
import jb.trainer.SvmTrainer;
import jb.trainer.Trainer;
import jb.validator.SimpleScoreValidator;
import jb.validator.Validator;

import java.io.IOException;
import java.util.*;

import static jb.util.BUtils.getAllPermutations;
import static jb.util.MathUtils.getCombinationsOfTwo;

public class Runner {

    static FileHelper fileHelper = new BReader();
    static Trainer trainer = new SvmTrainer();
    static Validator validator = new SimpleScoreValidator();
    static Selector selector = new NBestSelector();
    static Integrator integrator = new MeanIntegrator();

    public static void main(String[] args) throws IOException, InvalidInputDataException {


        List<int[]> combinations = getCombinationsOfTwo(11);
        Set<Double> finalScores = new TreeSet<>();
        for (int[] combination : combinations) {
            List<int[]> permutations = getAllPermutations();
            Map<Double, int[]> scores = new TreeMap<>();
            for (int[] permutation : permutations) {
                Opts opts = Opts.builder().filePath("src/main/resources/target/7_" + permutation[0] + "_" + permutation[1] + "_converted.csv").bias(1).
                        numberOfBaseClassifiers(9).numberOfSelectedClassifiers(3).numberOfSpaceParts(3).solverType(SolverType.L2R_LR).C(1).eps(.01).
                        permutation(combination).build();
                Dataset dataset = fileHelper.readFile(opts);
                List<Model> clfs = trainer.train(dataset, opts);
                ValidatingTestingTuple validatingTestingTuple = dataset.getValidatingTestingTuple(opts);
                ScoreTuple scoreTuple = validator.validate(clfs, validatingTestingTuple, opts);
                SelectedTuple selectedTuple = selector.select(scoreTuple, opts);
                IntegratedModel integratedModel = integrator.integrate(selectedTuple, clfs, dataset, opts);
                IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();
                scores.put(integratedScoreTester.test(integratedModel, validatingTestingTuple, opts), permutation);
            }
            finalScores.add(((TreeMap<Double, int[]>) scores).lastKey());
        }
        double sum = 0;
        for (Double finalScore : finalScores) {
            sum += finalScore;
        }
        System.out.println(sum / finalScores.size());
        System.out.println(((TreeSet<Double>) finalScores).last());

    }

}
