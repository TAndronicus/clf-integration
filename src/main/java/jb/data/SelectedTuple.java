package jb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectedTuple {

    /**
     * indices[numberOfSpaceParts][numberOfSelectedClassifiers]
     */
    private int[][] indices;
    /**
     * weights[numberOfSpaceParts][numberOfSelectedClassifiers]
     */
    private double[][] weights;

}
