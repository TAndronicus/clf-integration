package jb.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    public void testShouldReturnVectorProduct() {

        // given
        double[] v0 = {0, 1, 2, 3};
        double[] v1 = {4, 3, 2, 1};

        // when
        double product = MathUtils.INSTANCE.vectorProduct(v0, v1);

        // then
        assertThat(product, is(equalTo(10.0)));
    }

    @Test
    public void testShouldReturnTrace() {

        // given
        double[] vector = {.5, .7, .9, 1.1};

        // when
        double trace = MathUtils.INSTANCE.vectorTrace(vector);

        // then
        assertThat(trace, is(equalTo(3.2)));
    }

    @Test
    public void testShouldReturnRightCombinations() {

        // given
        int range = (int) (100 * Math.random());

        // when
        List<int[]> combinations = MathUtils.INSTANCE.getCombinationsOfTwo(range);

        // then
        assertThat(combinations.size(), is(equalTo(range * (range - 1))));
    }

    @Test
    public void testNoPermutationsOneEl() {

        // given
        int[] v0 = {2};
        int[] v1 = {3};

        // when
        boolean arePermutations = MathUtils.INSTANCE.arePermutations(v0, v1);

        // then
        assertThat(arePermutations, is(not(true)));
    }

    @Test
    public void testPermutationsOneEl() {

        // given
        int[] v0 = {2};
        int[] v1 = {2};

        // when
        boolean arePermutations = MathUtils.INSTANCE.arePermutations(v0, v1);

        // then
        assertThat(arePermutations, is(true));
    }

    @Test
    public void testNoPermutationsTwoEl() {

        // given
        int[] v0 = {2, 1};
        int[] v1 = {3, 2};

        // when
        boolean arePermutations = MathUtils.INSTANCE.arePermutations(v0, v1);

        // then
        assertThat(arePermutations, is(not(true)));
    }

    @Test
    public void testPermutationsTwoEl() {

        // given
        int[] v0 = {2, 3};
        int[] v1 = {3, 2};

        // when
        boolean arePermutations = MathUtils.INSTANCE.arePermutations(v0, v1);

        // then
        assertThat(arePermutations, is(true));
    }

}