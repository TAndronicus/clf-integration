package jb.integrator;

import jb.config.Opts;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.data.SelectedTuple;
import jb.util.ModelUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModelUtils.class)
public class MedianIntegratorTest {

    @Test
    public void shouldCalculateRightModel() {
        // given
        int[][] indices = {{0, 1, 2}, {1, 2, 3}};
        SelectedTuple selectedTuple = new SelectedTuple(indices, new double[][]{});

        double[] as = {-1, 1, 0, 0};
        double[] bs = {-1, 0, .5, 2};
        mockStatic(ModelUtils.class);
        when(ModelUtils.getAs(anyList())).thenReturn(as);
        when(ModelUtils.getBs(anyList())).thenReturn(bs);

        double minX = -1;
        double maxX = 1;
        Dataset dataset = new Dataset(null, minX, maxX);
        Opts opts = Opts.builder().numberOfSpaceParts(2).build();

        double[] expectedAs = {-.5, .5};
        double[] expectedBs = {-.25, .25};
        double[] expectedX = {-1, -.5, 1};

        // when
        Integrator integrator = new MedianIntegrator();
        IntegratedModel integratedModel = integrator.integrate(selectedTuple, new ArrayList<>(), dataset, opts);

        // then
        assertThat(integratedModel, is(notNullValue()));
        assertThat(integratedModel.getA().length, is(equalTo(expectedAs.length)));
        assertThat(integratedModel.getB().length, is(equalTo(expectedBs.length)));
        assertThat(integratedModel.getX().length, is(equalTo(expectedX.length)));
        for (int i = 0; i < expectedAs.length; i++) {
            assertThat(integratedModel.getA()[i], is(equalTo(expectedAs[i])));
            assertThat(integratedModel.getB()[i], is(equalTo(expectedBs[i])));
            assertThat(integratedModel.getX()[i], is(equalTo(expectedX[i])));
        }
        assertThat(integratedModel.getX()[integratedModel.getX().length - 1], is(equalTo(expectedX[expectedX.length - 1])));

    }

}
