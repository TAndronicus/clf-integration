package jb.data;

import de.bwaldvogel.liblinear.Feature;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClfObject implements Comparable<ClfObject>{

    private Feature[] x;
    private double y;
    @Override
    public int compareTo(ClfObject o) {
        return y != o.getY() ? Double.compare(y, o.getY()) : Double.compare(x[0].getValue(), o.getX()[0].getValue());
    }
}
