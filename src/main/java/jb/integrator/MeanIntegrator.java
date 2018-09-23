package jb.integrator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.data.SelectedTuple;
import jb.util.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jb.util.ModelUtils.getAs;
import static jb.util.ModelUtils.getBs;

public class MeanIntegrator implements Integrator {

    @Override
    public IntegratedModel integrate(SelectedTuple selectedTuple, List<Model> clfs, Dataset dataset, Opts opts) {
        
        double[] a = new double[opts.getNumberOfSpaceParts()];
        double[] b = new double[opts.getNumberOfSpaceParts()];
        double[] x = new double[opts.getNumberOfSpaceParts() + 1];
        x[0] = dataset.getMinX();
        x[opts.getNumberOfSpaceParts()] = dataset.getMaxX();
        for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
            List<Model> selectedClfs = getSelectedClfs(clfs, selectedTuple.getIndices()[i]);
            double[] as = getAs(selectedClfs);
            double[] bs = getBs(selectedClfs);
            a[i] = MathUtils.vectorProduct(as, selectedTuple.getWeights()[i]) / MathUtils.vectorTrace(selectedTuple.getWeights()[i]);
            b[i] = MathUtils.vectorProduct(bs, selectedTuple.getWeights()[i]) / MathUtils.vectorTrace(selectedTuple.getWeights()[i]);
            if (i != opts.getNumberOfSpaceParts() - 1) x[i + 1] = dataset.getMinX() + (i + 1) * (dataset.getMaxX() - dataset.getMinX()) / opts.getNumberOfSpaceParts();
        }
        return IntegratedModel.builder().a(a).b(b).x(x).build();
    }

    private List<Model> getSelectedClfs(List<Model> clfs, int[] index) {
        List<Model> selectedClfs = new ArrayList<>(index.length);
        for (int i = 0; i < clfs.size(); i++) {
            if (Arrays.binarySearch(index, i) >= 0) {
                selectedClfs.add(clfs.get(i));
            }
        }
        return selectedClfs;
    }

}
