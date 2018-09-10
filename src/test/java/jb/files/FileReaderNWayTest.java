package jb.files;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Constants;
import jb.config.Opts;
import jb.data.Dataset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FileReaderNWayTest {

    private static final String filenameTest0 = "src/test/resources/test0.txt";
    private static final String filenameTest1 = "src/test/resources/test1.txt";
    private static final int bias = 1;
    private static final int numberOfBaseClassifiers = 3;
    private static final int numberOfSpaceParts = 3;
    private static Opts opts = null;

    @BeforeAll
    public static void initialize() {

        opts = Opts.builder().bias(bias).numberOfBaseClassifiers(numberOfBaseClassifiers).numberOfSpaceParts(numberOfSpaceParts).build();

    }

    @Test
    public void shouldSplitDataIntoRightAmountOfSubspacesTest0() throws IOException, InvalidInputDataException {

        // given
        opts.setFilename(filenameTest0);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        // then
        assertThat(dataset.getTrainingProblems().size(), is(equalTo(numberOfBaseClassifiers)));
        assertThat(dataset.getValidatingProblems().size(), is(equalTo(numberOfSpaceParts)));

    }

    @Test
    public void shouldSplitDataIntoRightAmountOfSubspacesTest1() throws IOException, InvalidInputDataException {

        // given
        opts.setFilename(filenameTest1);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        // then
        assertThat(dataset.getTrainingProblems().size(), is(equalTo(numberOfBaseClassifiers)));
        assertThat(dataset.getValidatingProblems().size(), is(equalTo(numberOfSpaceParts)));

    }

    @Test
    public void shouldSortDataTest0() throws IOException, InvalidInputDataException {

        // given
        opts.setFilename(filenameTest0);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        // then
        for (int i = 0; i < dataset.getTrainingProblems().size(); i++) {
            assertThat(dataset.getTrainingProblems().get(i).y[0], is(equalTo(0.0)));
            assertThat(dataset.getTrainingProblems().get(i).y[1], is(equalTo(1.0)));
        }
        for (int i = 0; i < dataset.getTrainingProblems().size() - 1; i++) {
            assertThat(dataset.getTrainingProblems().get(i).x[0][0].getValue(),
                    is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[0][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).x[1][0].getValue(),
                    is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[1][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).y[0], is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).y[0])));
            assertThat(dataset.getTrainingProblems().get(i).y[1], is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).y[1])));
        }
        for (int i = 0; i < dataset.getTrainingProblems().size(); i++) {
            assertThat(dataset.getTrainingProblems().get(i).x[0][0].getValue(), is(lessThanOrEqualTo(dataset.getTestingProblem().x[0][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).x[1][0].getValue(), is(lessThanOrEqualTo(dataset.getTestingProblem().x[1][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).y[0], is(lessThanOrEqualTo(dataset.getTestingProblem().y[0])));
            assertThat(dataset.getTrainingProblems().get(i).y[1], is(lessThanOrEqualTo(dataset.getTestingProblem().y[1])));
        }

    }

    @Test
    public void shouldSortDataTest1() throws IOException, InvalidInputDataException {

        // given
        opts.setFilename(filenameTest1);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        //then
        for (int i = 0; i < dataset.getTrainingProblems().size() - 1; i++) {
            assertThat(dataset.getTrainingProblems().get(i).x[0][0].getValue(),
                    is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[0][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).x[1][0].getValue(),
                    is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[1][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).y[0], is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).y[0])));
            assertThat(dataset.getTrainingProblems().get(i).y[1], is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).y[1])));
        }
        for (int i = 0; i < dataset.getTrainingProblems().size(); i++) {
            assertThat(dataset.getTrainingProblems().get(i).x[0][0].getValue(), is(lessThanOrEqualTo(dataset.getTestingProblem().x[0][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).x[1][0].getValue(), is(lessThanOrEqualTo(dataset.getTestingProblem().x[1][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).y[0], is(lessThanOrEqualTo(dataset.getTestingProblem().y[0])));
            assertThat(dataset.getTrainingProblems().get(i).y[1], is(lessThanOrEqualTo(dataset.getTestingProblem().y[1])));
        }

    }

    @Test
    public void shouldSplitCorrectlyAmongValidationProblems() throws IOException, InvalidInputDataException {

        // given
        opts.setFilename(filenameTest1);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        //then
        dataset.getTrainingProblems().forEach(problem -> assertThat(problem.l, is(equalTo(6))));
        assertThat(dataset.getValidatingProblems().size(), is(equalTo(numberOfSpaceParts)));
        dataset.getValidatingProblems().forEach(problem -> assertThat(problem.l, is(equalTo(2))));
        for (Problem problem : dataset.getValidatingProblems()) {
            for (Feature[] features : problem.x) {
                assertThat(Math.floorMod((int) features[0].getValue(), 5), is(equalTo(3)));
            }
        }
        for (int i = 0; i < dataset.getValidatingProblems().size() - 1; i++) {
            for (int j = 0; j < dataset.getValidatingProblems().get(i).l; j++) {
                assertThat(dataset.getValidatingProblems().get(i).x[j][0].getValue(),
                        is(lessThanOrEqualTo(dataset.getValidatingProblems().get(i + 1).x[0][0].getValue())));
                assertThat(dataset.getValidatingProblems().get(i + 1).x[j][0].getValue(),
                        is(greaterThanOrEqualTo(dataset.getValidatingProblems().get(i).x[dataset.getValidatingProblems().get(i).l - 1][0].getValue())));
            }
        }
        for (int i = 0; i < dataset.getValidatingProblems().size(); i++) {
            for (int j = 0; j < dataset.getValidatingProblems().get(i).l; j++) {
                assertThat((int) (numberOfSpaceParts * (dataset.getValidatingProblems().get(i).x[j][0].getValue() - dataset.getMinX()) /
                        (dataset.getMaxX() - dataset.getMinX()) - Constants.EPSILON), is(equalTo(i)));
            }
        }
    }
}
