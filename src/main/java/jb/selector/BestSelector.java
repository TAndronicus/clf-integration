package jb.selector;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.ClfTuple;
import jb.data.ScoreTuple;

import java.util.List;

public class BestSelector implements Selector{

    @Override
    public ClfTuple select(List<? extends Model> clfs, ScoreTuple validationResults, Opts opts) {
        return null;
    }

}
