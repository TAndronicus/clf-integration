package validator;

import config.Opts;
import data.Dataset;
import data.ScoreTuple;
import de.bwaldvogel.liblinear.Model;

import java.util.List;

@FunctionalInterface
public interface Validator {

    ScoreTuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts);

}
