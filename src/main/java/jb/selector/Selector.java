package jb.selector;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.ScoreTuple;
import jb.data.SelectedTuple;

@FunctionalInterface
public interface Selector {

    SelectedTuple select(ScoreTuple validationResults, Opts opts);

}
