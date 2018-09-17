package jb.data.clfobj;

public class ClfObjectDoubleSorted extends ClfObject {

    public ClfObjectDoubleSorted(double[] x, int y) {
        super(x, y);
    }

    @Override
    public int compareTo(ClfObject o) {
        return y != o.getY() ? Integer.compare(y, o.getY()) : Double.compare(x[0], o.getX()[0]);
    }

    public ClfObjectOnceSorted convertToOnceSorted() {
        return new ClfObjectOnceSorted(this.getX(), this.getY());
    }
}
