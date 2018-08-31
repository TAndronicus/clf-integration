package validator;

import config.Opts;
import data.Dataset;
import data.Tuple;
import de.bwaldvogel.liblinear.Model;

import java.util.List;

public class SimpleScoreValidator implements Validator{
    @Override
    public Tuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts) {
        return null;
    }
}
