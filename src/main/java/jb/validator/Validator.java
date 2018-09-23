package jb.validator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.ScoreTuple;
import jb.data.ValidatingTestingTuple;

import java.util.List;

@FunctionalInterface
public interface Validator {

    ScoreTuple validate(List<? extends Model> clfs, ValidatingTestingTuple validatingTestingTuple, Opts opts);

}
