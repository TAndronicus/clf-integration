package jb.trainer;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;

import java.util.List;

@FunctionalInterface
public interface Trainer {

    List<Model> train(Dataset dataset, Opts opts);
}
