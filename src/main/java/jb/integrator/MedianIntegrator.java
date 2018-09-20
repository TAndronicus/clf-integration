package jb.integrator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.data.SelectedTuple;

import java.util.List;

public class MedianIntegrator implements Integrator{
    @Override
    public IntegratedModel integrate(SelectedTuple selectedTuple, List<Model> clfs, Dataset dataset, Opts opts) {
        return null;
    }
}
