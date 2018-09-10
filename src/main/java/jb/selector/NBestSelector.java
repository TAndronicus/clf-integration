package jb.selector;

import jb.config.Opts;
import jb.data.ScoreTuple;
import jb.data.SelectedTuple;

import java.util.Arrays;

public class NBestSelector implements Selector {

    @Override
    public SelectedTuple select(ScoreTuple validationResults, Opts opts) {

        int[][] indices = new int[opts.getNumberOfSpaceParts()][opts.getNumberOfSelectedClassifiers()];
        double[][] weights = new double[opts.getNumberOfSpaceParts()][opts.getNumberOfSelectedClassifiers()];
        if (opts.getNumberOfBaseClassifiers() == opts.getNumberOfSelectedClassifiers()) {
            for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
                for (int j = 0; j < opts.getNumberOfSelectedClassifiers(); j++) {
                    indices[i][j] = j;
                }
            }
            return new SelectedTuple(indices, validationResults.getWeights());
        }
        for (int i = 0; i < opts.getNumberOfSpaceParts(); i++) {
            double[] weightsProSubspace = validationResults.getWeights()[i];
            double[] copyOfWeightsProSubspace = Arrays.copyOf(weightsProSubspace, weightsProSubspace.length);
            Arrays.sort(copyOfWeightsProSubspace);
            double[] cutCopyOfWeightsProSubspace = Arrays.copyOfRange(copyOfWeightsProSubspace, opts.getNumberOfBaseClassifiers() - opts.getNumberOfSelectedClassifiers(), copyOfWeightsProSubspace.length);
            int counter = 0;
            for (int j = 0; j < opts.getNumberOfBaseClassifiers(); j++) {
                if (counter == opts.getNumberOfSelectedClassifiers()) {
                    break;
                }
                if (Arrays.binarySearch(cutCopyOfWeightsProSubspace, weightsProSubspace[j]) >= 0) {
                    indices[i][counter] = j;
                    weights[i][counter] = weightsProSubspace[j];
                    counter++;
                }
            }
        }
        return new SelectedTuple(indices, weights);
    }

}
