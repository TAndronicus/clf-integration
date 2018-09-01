package data;

import de.bwaldvogel.liblinear.Model;
import lombok.Data;

import java.util.List;

@Data
public class ClfTuple {

    private List<? extends Model> clfs;
    private double[] weights;

}
