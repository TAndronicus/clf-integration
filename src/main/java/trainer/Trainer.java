package trainer;

import config.Opts;
import data.Dataset;
import de.bwaldvogel.liblinear.Model;

import java.util.List;

@FunctionalInterface
public interface Trainer {

    List<Model> train(Dataset dataset, Opts opts);
}
