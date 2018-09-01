package files;

import data.Dataset;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FileReader10Way implements FileHelper{
    @Override
    public Dataset readFile(String filename) throws IOException, InvalidInputDataException {
        return null;
    }
}
