package jb.data;

import de.bwaldvogel.liblinear.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Dataset {

    private List<Problem> trainingProblems;
    private List<Problem> validatingProblems;
    private Problem testingProblem;
    private double minX;
    private double maxX;

}
