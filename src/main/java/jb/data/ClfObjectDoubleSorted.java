package jb.data;

import de.bwaldvogel.liblinear.Feature;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ClfObjectDoubleSorted extends ClfObject{

    public ClfObjectDoubleSorted(Feature[] x, double y) {
        super(x, y);
    }

    @Override
    public int compareTo(ClfObject o) {
        return y != o.getY() ? Double.compare(y, o.getY()) : Double.compare(x[0].getValue(), o.getX()[0].getValue());
    }
}
