package jb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreTuple {

    /**
     * scores[numberOfBaseClassifiers][numberOfSpaceParts]
     */
    private double[][] scores;
    /**
     * weight[numberOfSpaceParts][numberOfBaseClassifiers]
     */
    private double[][] weights;

}
