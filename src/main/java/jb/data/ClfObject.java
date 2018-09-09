package jb.data;

import de.bwaldvogel.liblinear.Feature;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class ClfObject implements Comparable<ClfObject> {

    protected Feature[] x;
    protected double y;

}
