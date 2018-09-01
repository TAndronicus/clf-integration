package integrator;

import config.Opts;
import data.ClfTuple;

@FunctionalInterface
public interface Integrator {

    double integrateScore(ClfTuple clfTuple, Opts opts);

}
