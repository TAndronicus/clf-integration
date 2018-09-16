package jb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidatingTestingTuple {

    private List<Problem> validationProblems;
    private Problem testingProblem;

}
