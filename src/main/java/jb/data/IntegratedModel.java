package jb.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntegratedModel {

    private double[] a;
    private double[] b;
    /**
     * Points between which the discriminant limit has the same function
     * Span                | a        | b
     * minX - x[0]         | a[0]     | b[0]
     * x[0] - x[1]         | a[1]     | b[1]
     * x[1] - x[2]         | a[2]     | b[2]
     * ...             | ...      | ...
     * x[n - 2] - x[n - 1] | a[n - 1] | b[n - 1]
     * x[n - 1] - maxX     | a[n]     | n[n]
     */
    private double[] x;

}
