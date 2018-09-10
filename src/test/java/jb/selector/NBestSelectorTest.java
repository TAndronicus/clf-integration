package jb.selector;

import jb.config.Opts;
import jb.data.ScoreTuple;
import jb.data.SelectedTuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NBestSelectorTest {

    @Mock
    Opts opts;

    @Test
    public void shouldReturnHighestWeights() {

        // given
        int numberOfSpaceParts = 3;
        int numberOfBaseClassifiers = 5;
        int numberOfSelectedClassifiers = 2;
        Selector selector = new NBestSelector();
        double[][] scores = {{.2, .3, .1, .5, .4}, {.5, .2, .1, .4, .3}, {.4, .4, .3, .5, .5}};
        double[][] weights = scores;
        ScoreTuple scoreTuple = new ScoreTuple(scores, weights);
        when(opts.getNumberOfSpaceParts()).thenReturn(numberOfSpaceParts);
        when(opts.getNumberOfBaseClassifiers()).thenReturn(numberOfBaseClassifiers);
        when(opts.getNumberOfSelectedClassifiers()).thenReturn(numberOfSelectedClassifiers);

        // when
        SelectedTuple selectedTuple = selector.select(scoreTuple, opts);

        // then
        assertThat(selectedTuple.getIndices()[0], is(equalTo(new int[]{3, 4})));
        assertThat(selectedTuple.getIndices()[1], is(equalTo(new int[]{0, 3})));
        assertThat(selectedTuple.getIndices()[2], is(equalTo(new int[]{3, 4})));
        assertThat(selectedTuple.getWeights()[0], is(equalTo(new double[]{.5, .4})));
        assertThat(selectedTuple.getWeights()[1], is(equalTo(new double[]{.5, .4})));
        assertThat(selectedTuple.getWeights()[2], is(equalTo(new double[]{.5, .5})));

    }

}
