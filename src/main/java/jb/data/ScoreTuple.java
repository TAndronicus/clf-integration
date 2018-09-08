package jb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreTuple {

    private double[] scores;
    private double[] weights;

}
