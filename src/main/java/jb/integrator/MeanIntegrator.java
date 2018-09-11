package jb.integrator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
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
    public IntegratedModel integrateScore(SelectedTuple selectedTuple, List<Model> clfs, Opts opts) {
        
        double[] a = new double[opts.getNumberOfSpaceParts()];
        double[] b = new double[opts.getNumberOfSpaceParts()];
        for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
            List<Model> selectedClfs = getSelectedClfs(clfs, selectedTuple.getIndices()[i]);
            double[] as = getAs(selectedClfs);
            double[] bs = getBs(selectedClfs);
            a[i] = MathUtils.vectorProduct(as, selectedTuple.getWeights()[i]) / MathUtils.vectorTrace(selectedTuple.getWeights()[i]);
            b[i] = MathUtils.vectorProduct(bs, selectedTuple.getWeights()[i]) / MathUtils.vectorTrace(selectedTuple.getWeights()[i]);
        }
        return IntegratedModel.builder().a(a).b(b).build();
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
