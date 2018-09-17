package jb.data.clfobj;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class ClfObject implements Comparable<ClfObject> {

    protected double[] x;
    protected int y;

}