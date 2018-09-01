import de.bwaldvogel.liblinear.InvalidInputDataException;
import org.junit.Test;

import java.io.IOException;

public class RunnerTest {

    @Test
    public void run() throws IOException, InvalidInputDataException {
        Runner runner = new Runner();
        runner.run();
    }

}
