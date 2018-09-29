package jb.data


import jb.config.Opts
import jb.data.clfobj.ClfObjectOnceSorted
import jb.util.ModelUtils.getIndexOfSubspace

data class Dataset(var problems: List<Problem>, var minX: Double, var maxX: Double) {

    fun getValidatingTestingTuple(opts: Opts): ValidatingTestingTuple {

        val (x1, y1) = this.problems[opts.permutation[0]]
        val validatingProblems = ArrayList<Problem>()
        val countOfSubspaceProblem = IntArray(opts.numberOfSpaceParts)
        val clfObjects = Array(y1.size) {_ -> ClfObjectOnceSorted(DoubleArray(x1[0].size), 0)}
        for (i in clfObjects.indices) {
            countOfSubspaceProblem[getIndexOfSubspace(opts.numberOfSpaceParts, x1[i][0], this.minX, this.maxX)]++
            clfObjects[i] = ClfObjectOnceSorted(x1[i], y1[i])
        }
        clfObjects.sort()
        var counter = 0
        for (i in countOfSubspaceProblem.indices) {
            val x = Array(countOfSubspaceProblem[i]) { DoubleArray(x1[0].size) }
            val y = IntArray(countOfSubspaceProblem[i])
            for (j in 0 until countOfSubspaceProblem[i]) {
                x[j] = clfObjects[counter].x
                y[j] = clfObjects[counter].y
                counter++
            }
            validatingProblems.add(Problem(x, y))
        }
        return ValidatingTestingTuple(validatingProblems, this.problems[opts.permutation[1]], this.minX, this.maxX)
    }

}
