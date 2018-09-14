package jb.files;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
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

import static jb.util.ModelUtils.getIndexOfSubspace;

public class FileReaderNWay implements FileHelper {

    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {

        Problem rootProblem = Problem.readFromFile(new File(opts.getFilename()), opts.getBias());
        ClfObjectDoubleSorted[] allObjects = getClfObjects(rootProblem);

        int numberOfSubsets = opts.getNumberOfBaseClassifiers() + 2;
        int countOfSubset = rootProblem.l / (numberOfSubsets);

        List<Problem> trainingProblems = new ArrayList<>();
        List<Problem> validatingProblems = new ArrayList<>();
        Problem testingProblem = null;

        int[] countOfValidatingObjects = new int[opts.getNumberOfSpaceParts()];
        ClfObjectOnceSorted[] clfObjectsOnceSorted = new ClfObjectOnceSorted[countOfSubset];

        ExtremeValues extremeValues = new ExtremeValues(allObjects).invoke();
        double xMin = extremeValues.getXMin();
        double xMax = extremeValues.getXMax();

        for (int i = 0; i < numberOfSubsets; i++) {
            if (i != numberOfSubsets - 2) {
                Feature[][] x = new Feature[countOfSubset][rootProblem.n];
                double[] y = new double[countOfSubset];
                for (int j = 0; j < countOfSubset; j++) {
                    x[j] = allObjects[j * numberOfSubsets + i].getX();
                    y[j] = allObjects[j * numberOfSubsets + i].getY();
                }
                Problem baseProblem = getBaseProblem(opts, rootProblem, countOfSubset, x, y);
                if (i == numberOfSubsets - 1) {
                    testingProblem = baseProblem;
                } else {
                    trainingProblems.add(baseProblem);
                }
            } else {
                for (int j = 0; j < countOfSubset; j++) {
                    countOfValidatingObjects[getIndexOfSubspace(opts.getNumberOfSpaceParts(), allObjects[j * numberOfSubsets + 1].getX()[0].getValue(), xMin, xMax)]++;
                    clfObjectsOnceSorted[j] = allObjects[j * numberOfSubsets + i].convertToOnceSorted();
                }
            }
        }

        Arrays.sort(clfObjectsOnceSorted);
        int counter = 0;

        for (int i = 0; i < countOfValidatingObjects.length; i++) {
            Feature[][] x = new Feature[countOfValidatingObjects[i]][rootProblem.n];
            double[] y = new double[countOfValidatingObjects[i]];
            for (int j = 0; j < countOfValidatingObjects[i]; j++) {
                ClfObject clfObjectDoubleSorted = clfObjectsOnceSorted[counter];
                x[j] = clfObjectDoubleSorted.getX();
                y[j] = clfObjectDoubleSorted.getY();
                counter++;
            }
            Problem baseProblem = getBaseProblem(opts, rootProblem, countOfValidatingObjects[i], x, y);
            validatingProblems.add(baseProblem);
        }
        return new Dataset(trainingProblems, validatingProblems, testingProblem, xMin, xMax);
    }

    private Problem getBaseProblem(Opts opts, Problem problem, int countOfSubset, Feature[][] x, double[] y) {
        Problem baseProblem = new Problem();
        baseProblem.bias = opts.getBias();
        baseProblem.x = x;
        baseProblem.y = y;
        baseProblem.n = problem.n;
        baseProblem.l = countOfSubset;
        return baseProblem;
    }

    private ClfObjectDoubleSorted[] getClfObjects(Problem problem) {
        ClfObjectDoubleSorted[] result = new ClfObjectDoubleSorted[problem.l];
        for (int i = 0; i < problem.l; i++) {
            result[i] = new ClfObjectDoubleSorted(problem.x[i], problem.y[i]);
        }
        Arrays.sort(result);
        return result;
    }

    @Data
    @RequiredArgsConstructor
    private class ExtremeValues {
        @NonNull
        private ClfObjectDoubleSorted[] clfObjectsDoubleSorted;
        private double xMin;
        private double xMax;

        public ExtremeValues invoke() {
            int classChangeIndex = 0;
            while (clfObjectsDoubleSorted[classChangeIndex].getY() == 0) {
                classChangeIndex++;
            }
            xMin = Math.min(clfObjectsDoubleSorted[0].getX()[0].getValue(), clfObjectsDoubleSorted[classChangeIndex].getX()[0].getValue());
            xMax = Math.max(clfObjectsDoubleSorted[clfObjectsDoubleSorted.length - 1].getX()[0].getValue(), clfObjectsDoubleSorted[classChangeIndex - 1].getX()[0].getValue());
            return this;
        }
    }
}
