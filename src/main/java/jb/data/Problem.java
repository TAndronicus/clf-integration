package jb.data;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Problem {

    double[][] x;
    int[] y;
    int bias;

    public de.bwaldvogel.liblinear.Problem convertToLibLinearProblem() {
        de.bwaldvogel.liblinear.Problem problem = new de.bwaldvogel.liblinear.Problem();
        problem.bias = this.bias;
        problem.l = x.length;
        problem.n = x[0].length;
        Feature[][] features = new Feature[x.length][x[0].length];
        double[] classes = new double[problem.l];
        for (int i = 0; i < problem.l; i++) {
            Feature[] baseFeature = new Feature[problem.n];
            for (int j = 0; j < problem.n; j++) {
                baseFeature[j] = new FeatureNode(j + 1, x[i][j]);
            }
            features[i] = baseFeature;
            classes[i] = this.y[i];
        }
        problem.x = features;
        problem.y = classes;
        return problem;
    }

}
