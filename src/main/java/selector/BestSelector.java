package selector;

import config.Opts;
import data.ClfTuple;
import data.ScoreTuple;
import de.bwaldvogel.liblinear.Model;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BestSelector implements Selector{

    @Override
    public ClfTuple select(List<? extends Model> clfs, ScoreTuple validationResults, Opts opts) {
        return null;
    }

}
