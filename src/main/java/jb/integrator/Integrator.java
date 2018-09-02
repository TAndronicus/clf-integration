package jb.integrator;

import jb.config.Opts;
import jb.data.ClfTuple;

@FunctionalInterface
public interface Integrator {

    double integrateScore(ClfTuple clfTuple, Opts opts);

}
