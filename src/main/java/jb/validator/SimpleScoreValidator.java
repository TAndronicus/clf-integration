package jb.validator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.ScoreTuple;

import java.util.List;

public class SimpleScoreValidator implements Validator {
    @Override
    public ScoreTuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts) {
        return null;
    }
}
