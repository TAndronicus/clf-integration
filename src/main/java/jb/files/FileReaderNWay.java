package jb.files;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Constants;
import jb.config.Opts;
import jb.data.ClfObject;
import jb.data.ClfObjectDoubleSorted;
import jb.data.ClfObjectOnceSorted;
import jb.data.Dataset;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReaderNWay implements FileHelper {

    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {

        Problem problem = Problem.readFromFile(new File(opts.getFilename()), opts.getBias());
        ClfObjectDoubleSorted[] clfObjectDoubleSorteds = getClfObjects(problem);

        int numberOfSubsets = opts.getNumberOfBaseClassifiers() + 2;
        int countOfSubset = problem.l / (numberOfSubsets);

        List<Problem> trainingProblems = new ArrayList<>();
        List<Problem> validatingProblems = new ArrayList<>();
        Problem testingProblem = null;

        int[] countOfValidatingObjects = new int[opts.getNumberOfSpaceParts()];
        ClfObjectOnceSorted[] clfObjectsOnceSorted = new ClfObjectOnceSorted[countOfSubset];

        ExtremeValues extremeValues = new ExtremeValues(clfObjectDoubleSorteds).invoke();
        double minX = extremeValues.getMinX();
        double maxX = extremeValues.getMaxX();

        for (int i = 0; i < numberOfSubsets; i++) {
            if (i != numberOfSubsets - 2) {
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
                } else {
                    trainingProblems.add(baseProblem);
                }
            } else {
                for (int j = 0; j < countOfSubset; j++) {
                    countOfValidatingObjects[(int) (opts.getNumberOfSpaceParts() * (clfObjectDoubleSorteds[j * numberOfSubsets + i].getX()[0].getValue() - minX) / (maxX - minX) - Constants.EPSILON)]++;
                    clfObjectsOnceSorted[j] = clfObjectDoubleSorteds[j * numberOfSubsets + i].convertToOnceSorted();
                }
            }
        }

        Arrays.sort(clfObjectsOnceSorted);
        int counter = 0;

        for (int i = 0; i < countOfValidatingObjects.length; i++) {
            Feature[][] x = new Feature[countOfValidatingObjects[i]][problem.n];
            double[] y = new double[countOfValidatingObjects[i]];
            for (int j = 0; j < countOfValidatingObjects[i]; j++) {
                ClfObject clfObjectDoubleSorted = clfObjectsOnceSorted[counter];
                x[j] = clfObjectDoubleSorted.getX();
                y[j] = clfObjectDoubleSorted.getY();
                counter++;
            }
            Problem baseProblem = new Problem();
            baseProblem.bias = opts.getBias();
            baseProblem.x = x;
            baseProblem.y = y;
            baseProblem.n = problem.n;
            baseProblem.l = countOfValidatingObjects[i];
            validatingProblems.add(baseProblem);
        }
        return new Dataset(trainingProblems, validatingProblems, testingProblem, minX, maxX);
    }

    private ClfObjectDoubleSorted[] getClfObjects(Problem problem) {
        ClfObjectDoubleSorted[] clfObjectDoubleSorteds = new ClfObjectDoubleSorted[problem.l];
        for (int i = 0; i < problem.l; i++) {
            clfObjectDoubleSorteds[i] = new ClfObjectDoubleSorted(problem.x[i], problem.y[i]);
        }
        Arrays.sort(clfObjectDoubleSorteds);
        return clfObjectDoubleSorteds;
    }

    @Data
    @RequiredArgsConstructor
    private class ExtremeValues {
        @NonNull
        private ClfObjectDoubleSorted[] clfObjectsDoubleSorted;
        private double minX;
        private double maxX;

        public ExtremeValues invoke() {
            int classChangeIndex = 0;
            while (clfObjectsDoubleSorted[classChangeIndex].getY() == 0) {
                classChangeIndex++;
            }
            minX = Math.min(clfObjectsDoubleSorted[0].getX()[0].getValue(), clfObjectsDoubleSorted[classChangeIndex].getX()[0].getValue());
            maxX = Math.max(clfObjectsDoubleSorted[clfObjectsDoubleSorted.length - 1].getX()[0].getValue(), clfObjectsDoubleSorted[classChangeIndex - 1].getX()[0].getValue());
            return this;
        }
    }
}
