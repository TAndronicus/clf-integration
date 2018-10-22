package jb.files

import de.bwaldvogel.liblinear.InvalidInputDataException
import de.bwaldvogel.liblinear.Problem
import jb.config.Opts
import jb.data.Dataset
import jb.data.clfobj.ClfObject
import jb.data.clfobj.ClfObjectDoubleSorted
import java.io.File
import java.io.IOException
import java.util.*

class BReader : FileHelper {
    @Throws(IOException::class, InvalidInputDataException::class)
    override fun readFile(opts: Opts): Dataset {
        val path = opts.filePath
        val rootProblem = Problem.readFromFile(File(path), opts.bias)
        val clfObjects = getClfObjects(rootProblem)

        val extremeValues = BReader.ExtremeValues(clfObjects).invoke()
        val xMin = extremeValues.xMin
        val xMax = extremeValues.xMax

        val clfObjectsTables = prepareLists()
        for (bClfObject in clfObjects) {
            clfObjectsTables[if (bClfObject.wt == 0) bClfObject.zb - 1 else 10].add(ClfObjectDoubleSorted(bClfObject.x, bClfObject.y))
        }
        val problems = ArrayList<jb.data.Problem>()
        for (objectList in clfObjectsTables) {
            Collections.sort(objectList)
            val x = Array(objectList.size) { DoubleArray(2) }
            val y = IntArray(objectList.size)
            for (i in objectList.indices) {
                x[i] = objectList[i].x
                y[i] = objectList[i].y
            }
            problems.add(jb.data.Problem(x, y))
        }

        return Dataset(problems, xMin, xMax)
    }

    private fun prepareLists(): ArrayList<ArrayList<ClfObject>> {
        val lists = ArrayList<ArrayList<ClfObject>>()
        for (i in 0..10) {
            lists.add(ArrayList())
        }
        return lists
    }

    private fun getClfObjects(problem: Problem): Array<BClfObject> {
        val result = Array(problem.l) {_ -> BClfObject(DoubleArray(problem.n - 2), 0, 0, 0)}
        for (i in 0 until problem.l) {
            val x = DoubleArray(problem.n - 2)
            var counter = 0
            for (j in 0 until problem.n) {
                if (j == problem.n - 3 || j == problem.n - 2) continue
                x[counter] = problem.x[i][j].value
                counter++
            }
            result[i] = BClfObject(x, problem.y[i].toInt(), problem.x[i][problem.n - 3].value.toInt(), problem.x[i][problem.n - 2].value.toInt())
        }
        return result
    }

    private data class ExtremeValues(var clfObjectsDoubleSorted: Array<BClfObject>) {

        var xMin: Double = 0.toDouble()
        var xMax: Double = 0.toDouble()

        operator fun invoke(): BReader.ExtremeValues {
            Arrays.sort(this.clfObjectsDoubleSorted)
            var classChangeIndex = 0
            while (this.clfObjectsDoubleSorted[classChangeIndex].y == 0) {
                classChangeIndex++
            }
            xMin = Math.min(this.clfObjectsDoubleSorted[0].x[0], this.clfObjectsDoubleSorted[classChangeIndex].x[0])
            xMax = Math.max(this.clfObjectsDoubleSorted[this.clfObjectsDoubleSorted.size - 1].x[0], this.clfObjectsDoubleSorted[classChangeIndex - 1].x[0])
            return this
        }
    }

    private inner class BClfObject(x: DoubleArray, y: Int,
                                   /**
                                    * Index of 10x validation
                                    */
                                   val zb: Int,
                                   /**
                                    * `0` for training, `1` for testing
                                    */
                                   val wt: Int) : ClfObjectDoubleSorted(x, y)

}
