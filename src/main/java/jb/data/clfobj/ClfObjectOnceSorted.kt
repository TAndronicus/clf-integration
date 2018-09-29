package jb.data.clfobj

class ClfObjectOnceSorted(x: DoubleArray, y: Int) : ClfObject(x, y) {

    override fun compareTo(o: ClfObject): Int {
        return java.lang.Double.compare(x[0], o.x[0])
    }

    fun convertToDoubleSorted(): ClfObjectDoubleSorted {
        return ClfObjectDoubleSorted(this.x, this.y)
    }

}
