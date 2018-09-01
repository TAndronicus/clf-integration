import config.Opts;
import data.Dataset;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import files.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {"files", "integrator", "selector", "trainer", "validator"})
public class Runner {

    @Autowired
    FileHelper fileHelper;

    public void run() throws IOException, InvalidInputDataException {

        Opts opts = new Opts();
        //FileHelper fileHelper = new FileReader10Way();
        Dataset dataset = fileHelper.readFile("");

    }
}
