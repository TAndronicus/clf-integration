import de.bwaldvogel.liblinear.InvalidInputDataException;
import jb.config.Opts;
import jb.data.Dataset;
import jb.files.FileHelper;
import jb.files.FileReaderNWay;

import java.io.IOException;

public class FileReaderNWayTest {

    @Test
    public void shouldSortAndSplitData() throws IOException, InvalidInputDataException {
        // given
        String filename = "/home/jb/Downloads/data3.txt";
        int bias = 1;
        int numberOfBaseClassifiers = 3;
        Opts opts = new Opts();
        opts.setFilename(filename);
        opts.setBias(bias);
        opts.setNumberOfBaseClassifiers(numberOfBaseClassifiers);
        // when
        FileHelper fileHelper = new FileReaderNWay();
        Dataset dataset = fileHelper.readFile(opts);
        // then

    }

}
