package validator;

import config.Opts;
import data.Dataset;
import data.ScoreTuple;
import de.bwaldvogel.liblinear.Model;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleScoreValidator implements Validator{
    @Override
    public ScoreTuple validate(List<? extends Model> clfs, Dataset dataset, Opts opts) {
        return null;
    }
}
