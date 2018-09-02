package jb.data;

import de.bwaldvogel.liblinear.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dataset {

    private Problem trainingProblem;
    private Problem validatingProblem;
    private Problem testingProblem;

}
