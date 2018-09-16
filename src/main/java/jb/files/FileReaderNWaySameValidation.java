package jb.files;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Opts;
import jb.data.ClfObject;
import jb.data.ClfObjectDoubleSorted;
import jb.data.Dataset;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileReaderNWaySameValidation implements FileHelper {

    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {

        Problem problem = Problem.readFromFile(new File(opts.getFilePath()), opts.getBias());
        ClfObject[] clfObjectDoubleSorteds = new ClfObjectDoubleSorted[problem.l];
        for (int i = 0; i < problem.l; i++) {
            clfObjectDoubleSorteds[i] = new ClfObjectDoubleSorted(problem.x[i], problem.y[i]);
        }
        Arrays.sort(clfObjectDoubleSorteds);

        int numberOfSubsets = opts.getNumberOfBaseClassifiers() + 2;
        int countOfSubset = problem.l / (numberOfSubsets);
        List<Problem> trainingProblems = new ArrayList<>();
        List<Problem> validatingProblems = new ArrayList<>();
        Problem testingProblem = null;
        for (int i = 0; i < numberOfSubsets; i++) {
            Feature[][] x = new Feature[countOfSubset][problem.n];
            double[] y = new double[countOfSubset];
            for (int j = 0; j < countOfSubset; j++) {
                x[j] = clfObjectDoubleSorteds[j * numberOfSubsets + i].getX();
                y[j] = clfObjectDoubleSorteds[j * numberOfSubsets + i].getY();
            }
            Problem baseProblem = new Problem();
            baseProblem.bias = opts.getBias();
            baseProblem.x = x;
            baseProblem.y = y;
            baseProblem.n = problem.n;
            baseProblem.l = countOfSubset;
            if (i == numberOfSubsets - 1) {
                testingProblem = baseProblem;
            } else if (i == numberOfSubsets - 2) {
                for (int j = 0; j < opts.getNumberOfBaseClassifiers(); j++) {
                    validatingProblems.add(baseProblem);
                }
            } else {
                trainingProblems.add(baseProblem);
            }
        }
        return new Dataset(trainingProblems, validatingProblems, testingProblem, clfObjectDoubleSorteds[0].getX()[0].getValue(), clfObjectDoubleSorteds[clfObjectDoubleSorteds.length - 1].getX()[0].getValue());
    }

}
