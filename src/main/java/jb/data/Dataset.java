package jb.data;

import jb.config.Opts;
import jb.data.clfobj.ClfObject;
import jb.data.clfobj.ClfObjectOnceSorted;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.sort;
import static jb.util.ModelUtils.getIndexOfSubspace;

@Data
@AllArgsConstructor
public class Dataset {

    private List<Problem> problems;
    private double minX;
    private double maxX;

    public ValidatingTestingTuple getValidatingTestingTuple(Opts opts) {

        Problem basePrblem = problems.get(opts.getPermutation()[0]);
        List<Problem> validatingProblems = new ArrayList<>();
        int[] countOfSubspaceProblem = new int[opts.getNumberOfSpaceParts()];
        ClfObject[] clfObjects = new ClfObjectOnceSorted[basePrblem.getY().length];
        for (int i = 0; i < clfObjects.length; i++) {
            countOfSubspaceProblem[getIndexOfSubspace(opts.getNumberOfSpaceParts(), basePrblem.getX()[i][0], this.minX, this.maxX)]++;
            clfObjects[i] = new ClfObjectOnceSorted(basePrblem.getX()[i], basePrblem.getY()[i]);
        }
        sort(clfObjects);
        int counter = 0;
        for (int i = 0; i < countOfSubspaceProblem.length; i++) {
            double[][] x = new double[countOfSubspaceProblem[i]][basePrblem.getX()[0].length];
            int[] y = new int[countOfSubspaceProblem[i]];
            for (int j = 0; j < countOfSubspaceProblem[i]; j++) {
                x[j] = clfObjects[counter].getX();
                y[j] = clfObjects[counter].getY();
                counter++;
            }
            validatingProblems.add(new Problem(x, y));
        }
        return new ValidatingTestingTuple(validatingProblems, problems.get(opts.getPermutation()[1]), this.minX, this.maxX);
    }

}
