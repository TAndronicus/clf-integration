package selector;

import config.Opts;
import data.Tuple;
import de.bwaldvogel.liblinear.Model;

import java.util.List;

public class BestSelector implements Selector{

    @Override
    public Tuple select(List<? extends Model> clfs, Tuple validationResults, Opts opts) {
        return null;
    }

}
