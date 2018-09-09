package jb.data;

import de.bwaldvogel.liblinear.Feature;

public class ClfObjectOnceSorted extends ClfObject {

    public ClfObjectOnceSorted(Feature[] x, double y) {
        super(x, y);
    }

    @Override
    public int compareTo(ClfObject o) {
        return Double.compare(x[0].getValue(), o.getX()[0].getValue());
    }

    public ClfObjectDoubleSorted convertToDoubleSorted() {
        return new ClfObjectDoubleSorted(this.getX(), this.getY());
    }

}
