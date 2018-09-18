package jb.runner;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import jb.config.Opts;
import jb.data.*;
import jb.files.FileHelper;
import jb.files.SimpleFileReader;
import jb.files.serialization.ModelReader;
import jb.files.serialization.ModelWriter;
import jb.integrator.Integrator;
import jb.integrator.MeanIntegrator;
import jb.selector.NBestSelector;
import jb.selector.Selector;
import jb.tester.IntegratedScoreTester;
import jb.trainer.SvmTrainer;
import jb.trainer.Trainer;
import jb.validator.SimpleScoreValidator;
import jb.validator.Validator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static jb.util.MathUtils.getCombinationsOfTwo;

public class MultiRunner {

    static FileHelper fileHelper = new SimpleFileReader();
    static Trainer trainer = new SvmTrainer();
    static ModelReader modelReader = new ModelReader();
    static ModelWriter modelWriter = new ModelWriter();
    static Validator validator = new SimpleScoreValidator();
    static Selector selector = new NBestSelector();
    static Integrator integrator = new MeanIntegrator();
    static IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();

    public static void main(String[] args) throws IOException, InvalidInputDataException {

        String sourcePath = "src/main/resources/target";
        String resultPath = "src/main/resources/results";
        Opts opts = Opts.builder().bias(1).solverType(SolverType.L2R_LR).C(1).eps(.01).build();
        int[] numbersOfBaseClassifiers = {3, 5};//, 7, 9};
        int[] numbersOfSpaceParts = {3, 4};//, 5, 6, 7, 8, 9, 10};
        Date before = new Date();
        IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();

        for (int numberOfBaseClassifiers : numbersOfBaseClassifiers) {
            opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
            PrintWriter printWriter = new PrintWriter(resultPath + "/" + numberOfBaseClassifiers + ".csv");
            StringBuilder res = initializeResultStringBuilder(numbersOfSpaceParts);
            for (int numberOfSelectedClassifiers = 2; numberOfSelectedClassifiers <= numberOfBaseClassifiers; numberOfSelectedClassifiers++) {
                opts.setNumberOfSelectedClassifiers(numberOfSelectedClassifiers);
                File[] files = new File(sourcePath).listFiles();
                for (int numberOfFile = 0; numberOfFile < files.length; numberOfFile++) {
                    opts.setFilePath(files[numberOfFile].getPath());
                    if (numberOfFile == 0) res.append(numberOfSelectedClassifiers);
                    res.append("," + files[numberOfFile].getName().split("_")[0]);
                    for (int numberOfSpaceParts : numbersOfSpaceParts) {
                        opts.setNumberOfSpaceParts(numberOfSpaceParts);
                        Dataset dataset = fileHelper.readFile(opts);
                        List<Model> clfs = trainer.train(dataset, opts);
                        modelWriter.saveModels(clfs, opts);
                        List<int[]> combinations = getCombinationsOfTwo(numberOfBaseClassifiers + 2);
                        double score = 0;
                        for (int[] combination : combinations) {
                            opts.setPermutation(combination);
                            List<Model> restoredClfs = modelReader.read(opts);
                            ValidatingTestingTuple validatingTestingTuple = dataset.getValidatingTestingTuple(opts);
                            ScoreTuple scoreTuple = validator.validate(restoredClfs, validatingTestingTuple, opts);
                            SelectedTuple selectedTuple = selector.select(scoreTuple, opts);
                            IntegratedModel integratedModel = integrator.integrate(selectedTuple, restoredClfs, opts);
                            score += integratedScoreTester.test(integratedModel, validatingTestingTuple, opts);
                        }
                        score /= combinations.size();
                        res.append(",").append(score);
                    }
                    res.append("\n");
                }
            }
            printWriter.println(res.toString());
            printWriter.flush();
            printWriter.close();
        }

        /*for (File file : (new File(sourcePath)).listFiles()) {
            opts.setFilePath(file.getPath());
            for (int numberOfSpaceParts : numbersOfSpaceParts) {
                opts.setNumberOfSpaceParts(numberOfSpaceParts);
                for (int numberOfBaseClassifiers : numbersOfBaseClassifiers) {
                    opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
                    for (int numberOfSelectedClassifiers = 2; numberOfSelectedClassifiers <= numberOfBaseClassifiers; numberOfSelectedClassifiers++) {
                        opts.setNumberOfSelectedClassifiers(numberOfSelectedClassifiers);
                        System.out.println("File: " + file.getName());
                        opts.setFilePath(file.getPath());
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
        }*/

        Date after = new Date();

        System.out.println("Took " + ((after.getTime() - before.getTime()) / 1000));

    }

    private static StringBuilder initializeResultStringBuilder(int[] numbersOfSpaceParts) {
        StringBuilder res = new StringBuilder(",subspaces");
        IntStream.of(numbersOfSpaceParts).mapToObj(i -> "," + i).forEach(res::append);
        res.append("\nselected classifiers,filename");
        IntStream.of(numbersOfSpaceParts).mapToObj(i -> ",score").forEach(res::append);
        res.append("\n");
        return res;
    }
}
