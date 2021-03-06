package jb.files;

import jb.config.Opts;

public class FileReaderNWaySameValidationTest {

    private static final String filename = "src/test/resources/test0.txt";
    private static final int bias = 1;
    private static final int numberOfBaseClassifiers = 3;
    private static Opts opts = null;

    /*@BeforeAll
    public static void initialize() {
        opts = Opts.builder().filename(filename).bias(bias).numberOfBaseClassifiers(3).build();
    }

    @Test
    public void shouldSplitDataIntoRightAmountOfSubspaces() throws IOException, InvalidInputDataException {

        // given

        // when
        FileHelper fileHelper = new FileReaderNWaySameValidation();
        Dataset dataset = fileHelper.readFile(opts);

        // then
        assertThat(dataset.getTrainingProblems().size(), is(equalTo(numberOfBaseClassifiers)));
        assertThat(dataset.getTrainingProblems().size(), is(equalTo(dataset.getValidatingProblems().size())));
        for (int i = 0; i < dataset.getTrainingProblems().size() - 1; i++) {
            assertThat(dataset.getValidatingProblems().get(i), is(sameInstance(dataset.getValidatingProblems().get(i + 1))));
        }

    }

    @Test
    public void shouldSortData() throws IOException, InvalidInputDataException {

        // given

        // when
        FileHelper fileHelper = new FileReaderNWaySameValidation();
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

    }*/
}
