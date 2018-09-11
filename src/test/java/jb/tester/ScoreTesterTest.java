package jb.tester;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Problem;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ScoreTesterTest {

    @Test
    public void shouldReturnRightScore() {
        // given
        double[] a = {1, .5, 2};
        double[] b = {.5, 0, -.5};
        double minX = -1;
        double maxX = 2;
        IntegratedModel integratedModel = IntegratedModel.builder().a(a).b(b).build();
        Feature[] feature0 = {new FeatureNode(0, .2), new FeatureNode(1, 0)};
        Feature[] feature1 = {new FeatureNode(0, .5), new FeatureNode(1, .3)};
        Feature[] feature2 = {new FeatureNode(0, .8), new FeatureNode(1, .3)};
        Feature[] feature3 = {new FeatureNode(0, -.5), new FeatureNode(1, .1)};
        Feature[] feature4 = {new FeatureNode(0, 1.5), new FeatureNode(1, 2)};
        double[] y = {0, 0, 1, 1, 0}; // should be {0, 1, 0, 1, 0}, expected score 1 - 0.4 = 0.6
        Problem problem = new Problem();
        problem.x = new Feature[][]{feature0, feature1, feature2, feature3, feature4};
        problem.y = y;
        problem.l = problem.x.length;
        Dataset dataset = new Dataset(new ArrayList<>(), new ArrayList<>(), problem, minX, maxX);

        // when
        ScoreTester scoreTester = new ScoreTester();
        double score = scoreTester.test(integratedModel, dataset);

        // then
        assertThat(score, is(equalTo(0.6)));
    }

}
