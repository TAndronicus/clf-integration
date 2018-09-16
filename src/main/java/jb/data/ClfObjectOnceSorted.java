package jb.data;

public class ClfObjectOnceSorted extends ClfObject {

    public ClfObjectOnceSorted(double[] x, int y) {
        super(x, y);
    }

    @Override
    public int compareTo(ClfObject o) {
        return Double.compare(x[0], o.getX()[0]);
    }

    public ClfObjectDoubleSorted convertToDoubleSorted() {
        return new ClfObjectDoubleSorted(this.getX(), this.getY());
    }

}
