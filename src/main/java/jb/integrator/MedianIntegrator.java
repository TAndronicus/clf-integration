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

import static jb.util.MathUtils.arePermutations;
import static jb.util.ModelUtils.*;

public class MedianIntegrator implements Integrator {
    @Override
    public IntegratedModel integrate(SelectedTuple selectedTuple, List<Model> clfs, Dataset dataset, Opts opts) {

        double[] as = getAs(clfs);
        double[] bs = getBs(clfs);
        List<Double> duplicatingAs = new ArrayList<>();
        List<Double> duplicatingBs = new ArrayList<>();
        List<Double> duplicatingX = new ArrayList<>();
        for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
            List<Double> crosspoints = getAllCrosspoints(as, bs);
            List<Double> filteredCrosspoints = filterCrosspoints(crosspoints, dataset, i, opts);
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
                duplicatingAs.add(indices.length == 1 ? as[indices[0]] : (as[indices[0]] + as[indices[1]]) / 2);
                duplicatingBs.add(indices.length == 1 ? bs[indices[0]] : (bs[indices[0]] + bs[indices[1]]) / 2);
                duplicatingX.add(filteredCrosspoints.get(j));
                previousIndices = indices;
            }
        }
        duplicatingX.add(dataset.getMaxX());
        List<Double> resultAs = new ArrayList<>();
        List<Double> resultBs = new ArrayList<>();
        List<Double> resultX = new ArrayList<>();
        resultAs.add(duplicatingAs.get(0));
        resultBs.add(duplicatingBs.get(0));
        resultX.add(duplicatingX.get(0));
        for (int i = 0; i < duplicatingX.size() - 2; i++) {
            if (duplicatingAs.get(i).equals(duplicatingAs.get(i + 1)) && duplicatingBs.get(i).equals(duplicatingBs.get(i + 1))) continue;
            resultAs.add(duplicatingAs.get(i + 1));
            resultBs.add(duplicatingBs.get(i + 1));
            resultX.add(duplicatingX.get(i + 1));
        }
        resultX.add(duplicatingX.get(duplicatingX.size() - 1));
        return IntegratedModel.builder().minX(dataset.getMinX()).maxX(dataset.getMaxX()).
                a(resultAs.stream().mapToDouble(Double::doubleValue).toArray()).
                b(resultBs.stream().mapToDouble(Double::doubleValue).toArray()).
                x(resultX.stream().mapToDouble(Double::doubleValue).toArray()).build();
    }

    private boolean sameIndices(int[] previousIndices, int[] indices) {
        return previousIndices != null && previousIndices.length == indices.length && arePermutations(previousIndices, indices);
    }

    private int[] getMiddleIndices(List<FunctionValue> functionValues) {
        return functionValues.size() % 2 == 0 ?
                new int[]{functionValues.get(functionValues.size() / 2 - 1).getIndex(), functionValues.get(functionValues.size() / 2).getIndex()} :
                new int[]{functionValues.get((functionValues.size() - 1) / 2).getIndex()};
    }

    private List<Double> getAllCrosspoints(double[] as, double[] bs) {
        List<Double> x = new ArrayList<>();
        for (int i = 0; i < as.length; i++) {
            for (int j = 0; j < bs.length; j++) {
                if (i == j || as[i] == as[j]) continue;
                x.add((bs[i] - bs[j]) / (as[i] - as[j]));
            }
        }
        return x;
    }

    private List<Double> filterCrosspoints(List<Double> crosspoints, Dataset dataset, int index, Opts opts) {
        List<Double> filteredCrosspoints = new ArrayList<>();
        filteredCrosspoints.add(getLowerLimit(dataset, index, opts));
        filteredCrosspoints.add(getHigherLimit(dataset, index, opts));
        for (Double crosspoint : crosspoints) {
            if (crosspoint < getLowerLimit(dataset, index, opts) || crosspoint > getHigherLimit(dataset, index, opts) || filteredCrosspoints.contains(crosspoint)) continue;
            filteredCrosspoints.add(crosspoint);
        }
        return filteredCrosspoints;
    }

    private double getLowerLimit(Dataset dataset, int index, Opts opts) {
        return dataset.getMinX() + index * (dataset.getMaxX() - dataset.getMinX()) / opts.getNumberOfSpaceParts();
    }

    private double getHigherLimit(Dataset dataset, int index, Opts opts) {
        return dataset.getMinX() + (index + 1 ) * (dataset.getMaxX() - dataset.getMinX()) / opts.getNumberOfSpaceParts();
    }

}
