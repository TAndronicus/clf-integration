package jb.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntegratedModel {

    private double minX;
    private double maxX;
    private double[] a;
    private double[] b;

}
