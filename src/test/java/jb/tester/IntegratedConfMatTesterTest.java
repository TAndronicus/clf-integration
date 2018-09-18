package jb.tester;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Problem;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.util.ModelUtils;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;

public class IntegratedConfMatTesterTest {

    @Test
    public void testShouldCalculateRightConfMat() {

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
        //Dataset dataset = new Dataset(new ArrayList<>(), new ArrayList<>(), problem, minX, maxX);
        IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();
        //double score = integratedScoreTester.test(integratedModel, dataset);

        // when
        IntegratedConfMatTester integratedConfMatTester = new IntegratedConfMatTester();
        //int[][] confMat = integratedConfMatTester.test(integratedModel, dataset);
        //double scoreFromConfMat = ModelUtils.calculateScoreFromConfMat(confMat);

        // then
        /*assertThat(scoreFromConfMat, is(equalTo(score)));
        assertThat(confMat[0][0], is(equalTo(2)));
        assertThat(confMat[0][1], is(equalTo(1)));
        assertThat(confMat[1][0], is(equalTo(1)));
        assertThat(confMat[1][1], is(equalTo(1)));*/
    }
}
