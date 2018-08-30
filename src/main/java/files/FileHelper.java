package files;

import data.Dataset;
import de.bwaldvogel.liblinear.InvalidInputDataException;

import java.io.IOException;

@FunctionalInterface
public interface FileHelper {

    Dataset readFile(String filename) throws IOException, InvalidInputDataException;

}
