package validator;

import config.Opts;
import data.Dataset;
import data.Tuple;
import de.bwaldvogel.liblinear.Model;

import java.util.List;

@FunctionalInterface
public interface Validator {

    Tuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts);

}
