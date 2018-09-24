package jb.data.clfobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class ClfObject implements Comparable<ClfObject> {

    protected double[] x;
    protected int y;

}
