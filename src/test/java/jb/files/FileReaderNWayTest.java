package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Opts;
import jb.data.Dataset;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class FileReaderNWayTest {

    @Test
    public void shouldSplitDataIntoRightAmountOfSubspacesTest0() throws IOException, InvalidInputDataException {

        // given
        String filename = "src/test/resources/test0.txt";
        int bias = 1;
        int numberOfBaseClassifiers = 3;
        int numberOfSpaceParts = 3;
        Opts opts = new Opts();
        opts.setFilename(filename);
        opts.setBias(bias);
        opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
        opts.setNumberOfSpaceParts(numberOfSpaceParts);

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
        String filename = "src/test/resources/test1.txt";
        int bias = 1;
        int numberOfBaseClassifiers = 3;
        int numberOfSpaceParts = 3;
        Opts opts = new Opts();
        opts.setFilename(filename);
        opts.setBias(bias);
        opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
        opts.setNumberOfSpaceParts(numberOfSpaceParts);

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
        String filename = "src/test/resources/test0.txt";
        int bias = 1;
        int numberOfBaseClassifiers = 3;
        int numberOfSpaceParts = 3;
        Opts opts = new Opts();
        opts.setFilename(filename);
        opts.setBias(bias);
        opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
        opts.setNumberOfSpaceParts(numberOfSpaceParts);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        // then
        for (int i = 0; i < dataset.getTrainingProblems().size(); i++) {
            assertThat(dataset.getTrainingProblems().get(i).y[0], is(equalTo(0.0)));
            assertThat(dataset.getTrainingProblems().get(i).y[1], is(equalTo(1.0)));
        }
        for (int i = 0; i < dataset.getTrainingProblems().size() - 1; i++) {
            assertThat(dataset.getTrainingProblems().get(i).x[0][0].getValue(), is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[0][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).x[1][0].getValue(), is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[1][0].getValue())));
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
        String filename = "src/test/resources/test1.txt";
        int bias = 1;
        int numberOfBaseClassifiers = 3;
        int numberOfSpaceParts = 3;
        Opts opts = new Opts();
        opts.setFilename(filename);
        opts.setBias(bias);
        opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
        opts.setNumberOfSpaceParts(numberOfSpaceParts);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        //then
        for (int i = 0; i < dataset.getTrainingProblems().size() - 1; i++) {
            assertThat(dataset.getTrainingProblems().get(i).x[0][0].getValue(), is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[0][0].getValue())));
            assertThat(dataset.getTrainingProblems().get(i).x[1][0].getValue(), is(lessThanOrEqualTo(dataset.getTrainingProblems().get(i + 1).x[1][0].getValue())));
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
    public void shouldContainRightAmountOfObjects() throws IOException, InvalidInputDataException {

        // given
        String filename = "src/test/resources/test1.txt";
        int bias = 1;
        int numberOfBaseClassifiers = 3;
        int numberOfSpaceParts = 3;
        Opts opts = new Opts();
        opts.setFilename(filename);
        opts.setBias(bias);
        opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
        opts.setNumberOfSpaceParts(numberOfSpaceParts);

        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);

        //then
        dataset.getTrainingProblems().forEach(problem -> assertThat(problem.x.length, is(equalTo(6))));
        assertThat(dataset.getValidatingProblems().size(), is(equalTo(numberOfSpaceParts)));
        dataset.getValidatingProblems().forEach(problem -> assertThat(problem.x.length, is(equalTo(2))));
    }
}
