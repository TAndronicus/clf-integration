package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Opts;
import jb.data.Dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderSameDataset implements FileHelper {

    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {
        Problem problem = Problem.readFromFile(new File(opts.getFilename()), 1);
        List<Problem> trainingProblems = new ArrayList<>();
        for (int i = 0; i < opts.getNumberOfBaseClassifiers(); i++) {
            trainingProblems.add(problem);
        }
        List<Problem> validatingProblems = new ArrayList<>();
        for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
            validatingProblems.add(problem);
        }
        double minX = problem.x[0][0].getValue();
        double maxX = minX;
        for (int i = 1; i < problem.x.length; i++) {
            minX = Math.min(minX, problem.x[i][0].getValue());
            maxX = Math.max(maxX, problem.x[i][0].getValue());
        }
        return new Dataset(trainingProblems, validatingProblems, problem, minX, maxX);
    }

}
