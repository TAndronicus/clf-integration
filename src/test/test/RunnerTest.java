import de.bwaldvogel.liblinear.InvalidInputDataException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Runner.class)
@ContextConfiguration()
public class RunnerTest {

    @Test
    public void run() throws IOException, InvalidInputDataException {
        Runner runner = new Runner();
        runner.run();
    }

}
