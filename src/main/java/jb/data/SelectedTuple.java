package jb.data;

import de.bwaldvogel.liblinear.Model;
import lombok.Data;

import java.util.List;

@Data
public class SelectedTuple {

    private int[][] indices;
    private double[][] weights;

}
