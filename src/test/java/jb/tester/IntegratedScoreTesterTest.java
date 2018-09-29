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

public class IntegratedScoreTesterTest {

    @Test
    public void shouldReturnRightScore() {
        // given
        /*Opts opts = Opts.builder().permutation(new int[]{0, 0}).numberOfSpaceParts(3).build();
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

        // when
        IntegratedScoreTester integratedScoreTester = new IntegratedScoreTester();
        double score = integratedScoreTester.test(integratedModel, dataset.getValidatingTestingTuple(opts), opts);

        // then
        assertThat(score, is(equalTo(0.6)));*/
    }

}
