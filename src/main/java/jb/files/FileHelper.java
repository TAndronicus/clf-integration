package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import jb.config.Opts;
import jb.data.Dataset;

import java.io.IOException;

@FunctionalInterface
public interface FileHelper {

    Dataset readFile(Opts opts) throws IOException, InvalidInputDataException;

}
