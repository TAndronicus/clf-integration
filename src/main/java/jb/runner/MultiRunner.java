package jb.runner;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import jb.files.FileHelper;
import jb.files.AReader;
import jb.files.serialization.ModelReader;
import jb.files.serialization.ModelWriter;
import jb.integrator.Integrator;
import jb.integrator.MeanIntegrator;
import jb.selector.NBestSelector;
import jb.selector.Selector;
import jb.tester.IntegratedConfMatTester;
import jb.tester.MvConfMatTester;
import jb.trainer.SvmTrainer;
import jb.trainer.Trainer;
import jb.validator.SimpleScoreValidator;
import jb.validator.Validator;

import java.io.IOException;
import java.util.stream.IntStream;

import static jb.config.Constants.*;

public class MultiRunner {

    private static final int numberOfStatistics = 4;
    private static FileHelper fileHelper = new AReader();
    private static Trainer trainer = new SvmTrainer();
    private static ModelReader modelReader = new ModelReader();
    private static ModelWriter modelWriter = new ModelWriter();
    private static Validator validator = new SimpleScoreValidator();
    private static Selector selector = new NBestSelector();
    private static Integrator integrator = new MeanIntegrator();
    private static IntegratedConfMatTester integratedConfMatTester = new IntegratedConfMatTester();
    private static MvConfMatTester mvConfMatTester = new MvConfMatTester();

    public static void main(String[] args) throws IOException, InvalidInputDataException {

        /*Opts opts = Opts.builder().bias(1).solverType(SolverType.L2R_LR).C(1).eps(.01).build();
        int[] numbersOfBaseClassifiers = {3, 5, 7, 9};
        int[] numbersOfSpaceParts = {3, 4, 5, 6, 7, 8, 9, 10};
        File root = new File(resultPath);
        if (!root.exists()) root.mkdir();
        Date before = new Date();

        for (int numberOfBaseClassifiers : numbersOfBaseClassifiers) {
            opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
            try (PrintWriter printWriter = new PrintWriter(resultPath + "/" + numberOfBaseClassifiers + ".csv")) {
                StringBuilder res = initializeResultStringBuilder(numbersOfSpaceParts);
                for (int numberOfSelectedClassifiers = 2; numberOfSelectedClassifiers <= numberOfBaseClassifiers; numberOfSelectedClassifiers++) {
                    opts.setNumberOfSelectedClassifiers(numberOfSelectedClassifiers);
                    File[] files = new File(convertedSourcesPath).listFiles();
                    for (int numberOfFile = 0; numberOfFile < files.length; numberOfFile++) {
                        opts.setFilePath(files[numberOfFile].getPath());
                        if (numberOfFile == 0) res.append(numberOfSelectedClassifiers);
                        res.append(separator + files[numberOfFile].getName().split("_")[0]);
                        for (int numberOfSpaceParts : numbersOfSpaceParts) {
                            opts.setNumberOfSpaceParts(numberOfSpaceParts);
                            Dataset dataset = fileHelper.readFile(opts);
                            List<Model> clfs = trainer.train(dataset, opts);
                            modelWriter.saveModels(clfs, opts);
                            List<int[]> combinations = getCombinationsOfTwo(numberOfBaseClassifiers + 2);
                            double[] statistics = new double[numberOfStatistics]; // integratedScore, integratedMcc, mvScore, mvMcc
                            for (int[] combination : combinations) {
                                opts.setPermutation(combination);
                                List<Model> restoredClfs = pickModels(clfs, opts);
                                ValidatingTestingTuple validatingTestingTuple = dataset.getValidatingTestingTuple(opts);
                                ScoreTuple scoreTuple = validator.validate(restoredClfs, validatingTestingTuple, opts);
                                SelectedTuple selectedTuple = selector.select(scoreTuple, opts);
                                IntegratedModel integratedModel = integrator.integrate(selectedTuple, restoredClfs, dataset, opts);
                                int[][] integratedConfMat = integratedConfMatTester.test(integratedModel, validatingTestingTuple, opts);
                                statistics[0] += calculateScoreFromConfMat(integratedConfMat);
                                statistics[1] += calculateMccFromConfMat(integratedConfMat);
                                int[][] mvConfMat = mvConfMatTester.test(restoredClfs, validatingTestingTuple, opts);
                                statistics[2] += calculateScoreFromConfMat(mvConfMat);
                                statistics[3] += calculateMccFromConfMat(mvConfMat);
                            }
                            DoubleStream.of(statistics).map(i -> i / combinations.size()).mapToObj(i -> separator + i).forEach(res::append);
                        }
                        res.append("\n");
                    }
                }
                printWriter.println(res.toString());
                printWriter.flush();
            }
        }

        Date after = new Date();
        System.out.println("Took " + ((after.getTime() - before.getTime()) / 1000));
*/
    }

    /*private static StringBuilder initializeResultStringBuilder(int[] numbersOfSpaceParts) {
        StringBuilder res = new StringBuilder(INSTANCE.getSeparator() + "subspaces");
        IntStream.of(numbersOfSpaceParts).mapToObj(i -> INSTANCE.getSeparator() + i + new String(new char[numberOfStatistics - 1]).replace("\0", INSTANCE.getSeparator())).forEach(res::append);
        res.append("\nselected classifiers" + INSTANCE.getSeparator() + "filename");
        IntStream.of(numbersOfSpaceParts).mapToObj(i -> INSTANCE.getSeparator() + "i score" + INSTANCE.getSeparator() + "i mcc" + INSTANCE.getSeparator() + "mv score" + INSTANCE.getSeparator() + "mv mcc").forEach(res::append);
        res.append("\n");
        return res;
    }*/
}
