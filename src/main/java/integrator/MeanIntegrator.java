package integrator;

import config.Opts;
import data.ClfTuple;
import org.springframework.stereotype.Component;

@Component
public class MeanIntegrator implements Integrator{
    @Override
    public double integrateScore(ClfTuple clfTuple, Opts opts) {
        return 0;
    }
}
