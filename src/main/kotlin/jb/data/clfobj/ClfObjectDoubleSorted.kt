package jb.data.clfobj

open class ClfObjectDoubleSorted(x: DoubleArray, y: Int) : ClfObject(x, y) {

    override fun compareTo(o: ClfObject): Int {
        return if (y != o.y) Integer.compare(y, o.y) else java.lang.Double.compare(x[0], o.x[0])
    }

    fun convertToOnceSorted(): ClfObjectOnceSorted {
        return ClfObjectOnceSorted(this.x, this.y)
    }
}
