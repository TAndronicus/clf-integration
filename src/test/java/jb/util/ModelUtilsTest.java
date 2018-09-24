package jb.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;

public class ModelUtilsTest {

    @Test
    public void testShouldReturnRightSubspaceIndex() {

        // given
        double minX = 0;
        double maxX = 10;
        int numberOfSubspaces = 5;
        double[] testX = {0, 1, 5, 7, 10};
        int[] expectedIndices = {0, 0, 2, 3, 4};

        // when
        int[] indices = new int[expectedIndices.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = ModelUtils.getIndexOfSubspace(numberOfSubspaces, testX[i], minX, maxX);
        }

        // then
        for (int i = 0; i < indices.length; i++) {
            assertThat(indices[i], is(equalTo(expectedIndices[i])));
        }
    }

    @Test
    public void testShouldReturnRightSubspaceIndexVector() {

        // given
        double[] x = {1, 2, 3, 5, 8, 13};
        double[] samples = {1, 2.5, 9, 13};
        int[] expectedIndices = {0, 1, 4, 4};

        // when
        int[] indices = new int[samples.length];
        for (int i = 0; i < expectedIndices.length; i++) {
            indices[i] = ModelUtils.getIndexOfSubspace(x, samples[i]);
        }

        // then
        for (int i = 0; i < indices.length; i++) {
            assertThat(indices[i], is(equalTo(expectedIndices[i])));
        }

    }

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
