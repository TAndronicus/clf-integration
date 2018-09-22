package jb.integrator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.FunctionValue;
import jb.data.IntegratedModel;
import jb.data.SelectedTuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static jb.util.ModelUtils.*;

public class MedianIntegrator implements Integrator {
    @Override
    public IntegratedModel integrate(SelectedTuple selectedTuple, List<Model> clfs, Dataset dataset, Opts opts) {

        double[] as = getAs(clfs);
        double[] bs = getBs(clfs);
        List<Double> resultAs = new ArrayList<>();
        List<Double> resultBs = new ArrayList<>();
        List<Double> x = new ArrayList<>();
        for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
            List<Double> crosspoints = getAllCrosspoints(as, bs);
            List<Double> filteredCrosspoints = filterCrosspoints(crosspoints, dataset, i, opts);
            Collections.sort(filteredCrosspoints);
            filteredCrosspoints.sort(Double::compare);
            int[] previousIndices = null;
            for (int j = 0; j < filteredCrosspoints.size() - 1; j++) {
                List<FunctionValue> functionValues = new ArrayList<>();
                for (int k = 0; k < as.length; k++) {
                    functionValues.add(new FunctionValue(k, as[k] * (filteredCrosspoints.get(j) + filteredCrosspoints.get(j + 1)) / 2 + bs[k]));
                }
                functionValues.sort(Comparator.comparingDouble(FunctionValue::getValue));
                int[] indices = getMiddleIndices(functionValues);
                if (sameIndices(previousIndices, indices)) continue;
                resultAs.add(indices.length == 1 ? as[indices[0]] : (as[indices[0]] + as[indices[1]]) / 2);
                resultBs.add(indices.length == 1 ? bs[indices[0]] : (bs[indices[0]] + bs[indices[1]]) / 2);
                x.add(filteredCrosspoints.get(j));
                previousIndices = indices;
            }
        }
        return IntegratedModel.builder().minX(dataset.getMinX()).maxX(dataset.getMaxX()).
                a(resultAs.stream().mapToDouble(Double::doubleValue).toArray()).
                b(resultBs.stream().mapToDouble(Double::doubleValue).toArray()).
                x(x.stream().mapToDouble(Double::doubleValue).toArray()).build();
    }

    private boolean sameIndices(int[] previousIndices, int[] indices) {
        return previousIndices != null && previousIndices.length == indices.length && previousIndices[0] == indices[0] &&
                (indices.length == 1 || previousIndices[1] == indices[1]);
    }

    private int[] getMiddleIndices(List<FunctionValue> functionValues) {
        return functionValues.size() % 2 == 0 ?
                new int[]{functionValues.get(functionValues.size() / 2 - 1).getIndex(), functionValues.get(functionValues.size() / 2).getIndex()} :
                new int[]{functionValues.get((functionValues.size() - 1) / 2).getIndex()};
    }

}
