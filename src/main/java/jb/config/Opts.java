package jb.config;

import de.bwaldvogel.liblinear.SolverType;
import lombok.Data;

@Data
public class Opts {

    /**
     * Absolute path to file in LibSVM format
     */
    private String filename;
    /**
     * Non-positive bias means fixing classifier discriminator function in (0, 0, ..., 0)
     */
    private double bias;
    /**
     * Number of base classifiers
     */
    private int numberOfBaseClassifiers;
    /**
     * Number of selected classifiers
     */
    private int numberOfSelectedClassifiers;
    /**
     * Number of space parts
     */
    private int numberOfSpaceParts;

    /**
     * Type of classifier
     */
    private SolverType solverType;
    /**
     * Cost of constraints violation
     */
    private double C;
    /**
     * Stopping criteria
     */
    private double eps;

}
