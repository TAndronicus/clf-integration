package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.clfobj.ClfObject;
import jb.data.clfobj.ClfObjectDoubleSorted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReaderNWaySameValidation implements FileHelper {

    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {

        Problem problem = Problem.readFromFile(new File(opts.getFilePath()), opts.getBias());
        ClfObject[] clfObjectDoubleSorteds = new ClfObjectDoubleSorted[problem.l];
        for (int i = 0; i < problem.l; i++) {
            double[] x = new double[problem.n];
            for (int j = 0; j < problem.n; j++) {
                x[j] = problem.x[i][j].getValue();
            }
            clfObjectDoubleSorteds[i] = new ClfObjectDoubleSorted(x, (int) problem.y[i]);
        }
        Arrays.sort(clfObjectDoubleSorteds);

        int numberOfSubsets = opts.getNumberOfBaseClassifiers() + 2;
        int countOfSubset = problem.l / (numberOfSubsets);
        List<jb.data.Problem> problems = new ArrayList<>();
        for (int i = 0; i < numberOfSubsets; i++) {
            double[][] x = new double[countOfSubset][problem.n];
            int[] y = new int[countOfSubset];
            for (int j = 0; j < countOfSubset; j++) {
                x[j] = clfObjectDoubleSorteds[j * numberOfSubsets + i].getX();
                y[j] = clfObjectDoubleSorteds[j * numberOfSubsets + i].getY();
            }
            problems.add(new jb.data.Problem(x, y));
        }
        return new Dataset(problems, clfObjectDoubleSorteds[0].getX()[0], clfObjectDoubleSorteds[clfObjectDoubleSorteds.length - 1].getX()[0]);
    }

}
