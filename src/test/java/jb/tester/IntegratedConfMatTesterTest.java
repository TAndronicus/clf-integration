package jb.tester;

import jb.config.Opts;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.data.Problem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class IntegratedConfMatTesterTest {

    @Test
    public void testShouldCalculateRightConfMat() {

        // given
        /*Opts opts = Opts(permutation = int[]{0, 0}, numberOfSpaceParts = 3);
        double[] a = {1, .5, 2};
        double[] b = {.5, 0, -.5};
        double[] crosspoints = {-1, 0, 1, 2};
        double minX = -1;
        double maxX = 2;
        IntegratedModel integratedModel = IntegratedModel.builder().a(a).b(b).x(crosspoints).build();
        double[][] x = {
                {.2, 0},
                {.5, .3},
                {.8, .3},
                {-.5, .1},
                {1.5, 2}
        };
        int[] y = {0, 0, 1, 1, 0}; // should be {0, 1, 0, 1, 0}, expected score 1 - 0.4 = 0.6
        Problem problem = new Problem(x, y);
        Dataset dataset = new Dataset(new ArrayList<Problem>() {{
            add(problem);
        }}, minX, maxX);
        IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();
        double score = integratedScoreTester.test(integratedModel, dataset.getValidatingTestingTuple(opts), opts);

        // when
        IntegratedConfMatTester integratedConfMatTester = new IntegratedConfMatTester();
        int[][] confMat = integratedConfMatTester.test(integratedModel, dataset.getValidatingTestingTuple(opts), opts);
        double scoreFromConfMat = calculateScoreFromConfMat(confMat);

        // then
        assertThat(scoreFromConfMat, is(equalTo(score)));
        assertThat(confMat[0][0], is(equalTo(2)));
        assertThat(confMat[0][1], is(equalTo(1)));
        assertThat(confMat[1][0], is(equalTo(1)));
        assertThat(confMat[1][1], is(equalTo(1)));*/
    }
}
