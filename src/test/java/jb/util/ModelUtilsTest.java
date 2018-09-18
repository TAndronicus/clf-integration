package jb.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ModelUtilsTest {

    @Test
    public void testShouldCalculateRightScore() {

        // given
        int[][] confMat = {{(int) (Math.random() * 100), (int) (Math.random() * 100)}, {(int) (Math.random() * 100), (int) (Math.random() * 100)}};

        // when
        double score = ModelUtils.calculateScoreFromConfMat(confMat);

        // then
        assertThat(score, is(equalTo((.0 + confMat[0][0] + confMat[1][1]) / (confMat[0][0] + confMat[0][1] + confMat[1][0] + confMat[1][1]))));
    }

}
