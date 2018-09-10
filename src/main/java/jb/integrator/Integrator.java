package jb.integrator;

import jb.config.Opts;
import jb.data.SelectedTuple;

@FunctionalInterface
public interface Integrator {

    double integrateScore(SelectedTuple selectedTuple, Opts opts);

}
