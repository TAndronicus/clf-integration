package jb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectedTuple {

    private int[][] indices;
    private double[][] weights;

}
