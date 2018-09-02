package jb.validator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.ScoreTuple;

import java.util.List;

@FunctionalInterface
public interface Validator {

    ScoreTuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts);

}
