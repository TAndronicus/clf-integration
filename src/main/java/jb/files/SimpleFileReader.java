package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.clfobj.ClfObjectDoubleSorted;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleFileReader implements FileHelper {

    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {

        Problem rootProblem = Problem.readFromFile(new File(opts.getFilePath()), opts.getBias());
        ClfObjectDoubleSorted[] allObjects = getClfObjects(rootProblem);

        int numberOfSubsets = opts.getNumberOfBaseClassifiers() + 2;
        int countOfSubset = rootProblem.l / (numberOfSubsets);

        ExtremeValues extremeValues = (new ExtremeValues(allObjects)).invoke();
        double xMin = extremeValues.getXMin();
        double xMax = extremeValues.getXMax();

        List<jb.data.Problem> problems = new ArrayList<>();
        for (int i = 0; i < numberOfSubsets; i++) {
            double[][] x = new double[countOfSubset][rootProblem.n];
            int[] y = new int[countOfSubset];
            for (int j = 0; j < countOfSubset; j++) {
                x[j] = allObjects[j * numberOfSubsets + i].getX();
                y[j] = allObjects[j * numberOfSubsets + i].getY();
            }
            problems.add(new jb.data.Problem(x, y));
        }

        return new Dataset(problems, xMin, xMax);
    }

    private ClfObjectDoubleSorted[] getClfObjects(Problem problem) {
        ClfObjectDoubleSorted[] result = new ClfObjectDoubleSorted[problem.l];
        for (int i = 0; i < problem.l; i++) {
            double[] x = new double[problem.n];
            for (int j = 0; j < problem.n; j++) {
                x[j] = problem.x[i][j].getValue();
            }
            result[i] = new ClfObjectDoubleSorted(x, (int) problem.y[i]);
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

        public SimpleFileReader.ExtremeValues invoke() {
            int classChangeIndex = 0;
            while (clfObjectsDoubleSorted[classChangeIndex].getY() == 0) {
                classChangeIndex++;
            }
            xMin = Math.min(clfObjectsDoubleSorted[0].getX()[0], clfObjectsDoubleSorted[classChangeIndex].getX()[0]);
            xMax = Math.max(clfObjectsDoubleSorted[clfObjectsDoubleSorted.length - 1].getX()[0], clfObjectsDoubleSorted[classChangeIndex - 1].getX()[0]);
            return this;
        }
    }

}
