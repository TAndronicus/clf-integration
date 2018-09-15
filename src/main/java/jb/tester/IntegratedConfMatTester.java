package jb.tester;

import jb.data.Dataset;
import jb.data.IntegratedModel;

import static jb.util.ModelUtils.getIndexOfSubspace;

public class IntegratedConfMatTester {

    public int[][] test(IntegratedModel integratedModel, Dataset dataset) {
        int[][] confusionMatrix = {{0, 0}, {0, 0}};
        int realIndex = 0;
        int predictedIndex = 0;
        for (int i = 0; i < dataset.getTestingProblem().l; i++) {
            int index = getIndexOfSubspace(integratedModel.getA().length, dataset.getTestingProblem().x[i][0].getValue(), dataset.getMinX(), dataset.getMaxX());
            double value = integratedModel.getA()[index] * dataset.getTestingProblem().x[i][0].getValue() + integratedModel.getB()[index];
            realIndex = dataset.getTestingProblem().y[i] == 1 ? 1 : 0;
            predictedIndex = value > dataset.getTestingProblem().x[i][1].getValue() ? 1 : 0;
            confusionMatrix[realIndex][predictedIndex]++;
        }
        return confusionMatrix;
    }
}
