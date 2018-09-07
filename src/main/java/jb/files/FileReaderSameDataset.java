package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Opts;
import jb.data.Dataset;

import java.io.File;
import java.io.IOException;

public class FileReaderSameDataset implements FileHelper{

    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {
        Problem problem = Problem.readFromFile(new File(opts.getFilename()), 1);
        return new Dataset(problem, problem, problem);
    }

}
