import config.Opts;
import data.Dataset;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import files.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Runner {

    @Autowired
    FileHelper fileHelper;

    public void run() throws IOException, InvalidInputDataException {

        Opts opts = new Opts();
        Dataset dataset = fileHelper.readFile("");

    }
}
