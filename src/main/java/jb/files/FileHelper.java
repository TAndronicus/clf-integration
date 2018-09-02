package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import jb.data.Dataset;

import java.io.IOException;

@FunctionalInterface
public interface FileHelper {

    Dataset readFile(String filename) throws IOException, InvalidInputDataException;

}
