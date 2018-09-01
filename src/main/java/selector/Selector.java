package selector;

import config.Opts;
import data.ClfTuple;
import data.ScoreTuple;
import de.bwaldvogel.liblinear.Model;

import java.util.List;

@FunctionalInterface
public interface Selector {

    ClfTuple select(List<? extends Model> clfs, ScoreTuple validationResults, Opts opts);

}
