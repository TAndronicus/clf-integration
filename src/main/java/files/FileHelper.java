package files;

import data.Dataset;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;

import java.io.File;
import java.io.IOException;

@FunctionalInterface
public interface FileHelper {

    default Dataset readFile(String filename) throws IOException, InvalidInputDataException {
        Problem problem = Problem.readFromFile(new File(filename), 1);
        return new Dataset(problem, problem, problem);
    }

}
