package jb;

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

import java.io.IOException;
import java.util.List;

public class Runner {

    static FileHelper fileHelper = new SimpleFileReader();
    static Trainer trainer = new SvmTrainer();
    static Validator validator = new SimpleScoreValidator();
    static Selector selector = new NBestSelector();
    static Integrator integrator = new MeanIntegrator();
    static ModelWriter modelWriter = new ModelWriter();
    static ModelReader modelReader = new ModelReader();

    public static void main(String[] args) throws IOException, InvalidInputDataException {

        Opts opts = Opts.builder().filePath("src/main/resources/target/data_banknote_authentication_1_2_converted.csv").bias(1).numberOfBaseClassifiers(3).numberOfSelectedClassifiers(2).
                numberOfSpaceParts(3).solverType(SolverType.L2R_LR).C(1).eps(.01).permutation(new int[]{3, 4}).build();
        Dataset dataset = fileHelper.readFile(opts);
        List<Model> clfs = trainer.train(dataset, opts);
        modelWriter.saveModels(clfs, opts);
        List<Model> restoredClfs = modelReader.read(opts);
        System.out.println(restoredClfs.size());
        ValidatingTestingTuple validatingTestingTuple = dataset.getValidatingTestingTuple(opts);
        ScoreTuple scoreTuple = validator.validate(clfs, validatingTestingTuple, opts);
        SelectedTuple selectedTuple = selector.select(scoreTuple, opts);
        IntegratedModel integratedModel = integrator.integrate(selectedTuple, clfs, opts);
        IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();
        System.out.println(integratedScoreTester.test(integratedModel, validatingTestingTuple, opts));

    }

}
