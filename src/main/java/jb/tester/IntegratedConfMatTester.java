package jb.tester;

import jb.config.Opts;
import jb.data.IntegratedModel;
import jb.data.ValidatingTestingTuple;
import jb.util.ModelUtils;
import lombok.Data;

import java.util.Arrays;

import static jb.util.ModelUtils.getIndexOfSubspace;
import static jb.util.ModelUtils.switchConfMatColumns;

@Data
public class IntegratedConfMatTester {

    public int[][] test(IntegratedModel integratedModel, ValidatingTestingTuple validatingTestingTuple, Opts opts) {
        int[][] confusionMatrix = {{0, 0}, {0, 0}};
        int realIndex;
        int predictedIndex;
        for (int i = 0; i < validatingTestingTuple.getTestingProblem().getY().length; i++) {
            int index = getIndexOfSubspace(integratedModel.getX(), validatingTestingTuple.getTestingProblem().getX()[i][0]);
            double value = integratedModel.getA()[index] * validatingTestingTuple.getTestingProblem().getX()[i][0] + integratedModel.getB()[index];
            realIndex = validatingTestingTuple.getTestingProblem().getY()[i] == 1 ? 1 : 0;
            predictedIndex = value > validatingTestingTuple.getTestingProblem().getX()[i][1] ? 1 : 0;
            confusionMatrix[realIndex][predictedIndex]++;
        }
        if (ModelUtils.calculateScoreFromConfMat(confusionMatrix) < .5) {
            switchConfMatColumns(confusionMatrix);
        }
        return confusionMatrix;
    }


}
