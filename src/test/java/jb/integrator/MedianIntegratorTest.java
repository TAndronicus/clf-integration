package jb.integrator;

import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.IntegratedModel;
import jb.data.SelectedTuple;
import jb.util.ModelUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMock)
@PrepareForTest(ModelUtils.class)
public class MedianIntegratorTest {

    @Test
    public void shouldCalculateRightModel() {
        // given
        int[][] indices = {{0, 1, 2}, {1, 2, 3}};
        SelectedTuple selectedTuple = new SelectedTuple(indices, new double[][]{});

        List<Model> clfs = new ArrayList<>();
        double[] as = {-1, 1, 0, 0};
        double[] bs = {-1, 0, .5, 2};
        mockStatic(ModelUtils.class);
        when(ModelUtils.getAs(clfs)).thenReturn(as);
        when(ModelUtils.getBs(clfs)).thenReturn(bs);

        double minX = -1;
        double maxX = 1;
        Dataset dataset = new Dataset(null, minX, maxX);
        Opts opts = Opts.builder().build();

        // when
        Integrator integrator = new MedianIntegrator();
        IntegratedModel integratedModel = integrator.integrate(selectedTuple, clfs, dataset, opts);

        // then
        assertThat(integratedModel, is(notNullValue()));
    }

}
