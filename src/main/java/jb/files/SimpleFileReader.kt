package jb.files

import de.bwaldvogel.liblinear.InvalidInputDataException
import de.bwaldvogel.liblinear.Problem
import jb.config.Opts
import jb.data.Dataset
import jb.data.clfobj.ClfObjectDoubleSorted
import java.io.File
import java.io.IOException
import java.util.*

class SimpleFileReader : FileHelper {

    @Throws(IOException::class, InvalidInputDataException::class)
    override fun readFile(opts: Opts): Dataset {

        val rootProblem = Problem.readFromFile(File(opts.filePath), opts.bias)
        val allObjects: Array<ClfObjectDoubleSorted> = getClfObjects(rootProblem) ?: throw NullPointerException("Problem not read")

        val numberOfSubsets = opts.numberOfBaseClassifiers + 2
        val countOfSubset = rootProblem.l / numberOfSubsets

        val extremeValues = ExtremeValues(allObjects).invoke()
        val xMin = extremeValues.xMin
        val xMax = extremeValues.xMax

        val problems = ArrayList<jb.data.Problem>()
        for (i in 0 until numberOfSubsets) {
            val x = Array(countOfSubset) { DoubleArray(rootProblem.n) }
            val y = IntArray(countOfSubset)
            for (j in 0 until countOfSubset) {
                x[j] = allObjects[j * numberOfSubsets + i].x
                y[j] = allObjects[j * numberOfSubsets + i].y
            }
            problems.add(jb.data.Problem(x, y))
        }

        return Dataset(problems, xMin, xMax)
    }

    private fun getClfObjects(problem: Problem): Array<ClfObjectDoubleSorted>? {
        val result = Array(problem.l) { _ ->  ClfObjectDoubleSorted(DoubleArray(problem.n), 0)}
        for (i in 0 until problem.l) {
            val x = DoubleArray(problem.n)
            for (j in 0 until problem.n) {
                x[j] = problem.x[i][j].value
            }
            result[i] = ClfObjectDoubleSorted(x, problem.y[i].toInt())
        }
        return result
    }

    private data class ExtremeValues(var clfObjectsDoubleSorted: Array<ClfObjectDoubleSorted>) {

        var xMin: Double = 0.toDouble()
        var xMax: Double = 0.toDouble()

        operator fun invoke(): SimpleFileReader.ExtremeValues {
            var classChangeIndex = 0
            while (this.clfObjectsDoubleSorted[classChangeIndex].y == 0) {
                classChangeIndex++
            }
            xMin = Math.min(this.clfObjectsDoubleSorted[0].x[0], this.clfObjectsDoubleSorted[classChangeIndex].x[0])
            xMax = Math.max(this.clfObjectsDoubleSorted[this.clfObjectsDoubleSorted.size - 1].x[0], this.clfObjectsDoubleSorted[classChangeIndex - 1].x[0])
            return this
        }
    }

}
