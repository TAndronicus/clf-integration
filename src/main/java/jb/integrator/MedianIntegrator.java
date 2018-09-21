package jb.integrator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.FunctionValue;
import jb.data.IntegratedModel;
import jb.data.SelectedTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jb.util.ModelUtils.*;

public class MedianIntegrator implements Integrator{
    @Override
    public IntegratedModel integrate(SelectedTuple selectedTuple, List<Model> clfs, Dataset dataset, Opts opts) {

        double[] as = getAs(clfs);
        double[] bs = getBs(clfs);
        List<Double> x = new ArrayList<>();
        for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
            List<Double> crosspoints = getAllCrosspoints(as, bs);
            List<Double> filteredCrosspoints = filterCrosspoints(crosspoints, dataset, i, opts);
            // crosspoints sorting
            for (int j = 0; j < filteredCrosspoints.size() - 1; j++) {
                List<FunctionValue> functionValues = new ArrayList<>();
                for (int k = 0; k < as.length; k++) {
                    functionValues.add(new FunctionValue(k, as[k] * (crosspoints.get(j) + crosspoints.get(j + 1)) / 2 + bs[k]));
                }
            }
        }
        return null;
    }

}
