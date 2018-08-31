package selector;

import config.Opts;
import data.Tuple;
import de.bwaldvogel.liblinear.Model;

import java.util.List;

@FunctionalInterface
public interface Selector {

    Tuple select(List<? extends Model> clfs, Tuple validationResults, Opts opts);

}
