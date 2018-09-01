package trainer;

import config.Opts;
import data.Dataset;
import de.bwaldvogel.liblinear.Model;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SVMTrainer implements Trainer{

    @Override
    public List<Model> train(Dataset dataset, Opts opts) {
        return null;
    }

}
