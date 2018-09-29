package jb.files

import de.bwaldvogel.liblinear.Feature
import de.bwaldvogel.liblinear.FeatureNode
import de.bwaldvogel.liblinear.InvalidInputDataException
import de.bwaldvogel.liblinear.Problem
import jb.config.Opts
import jb.data.Dataset
import jb.data.clfobj.ClfObject
import jb.data.clfobj.ClfObjectDoubleSorted
import jb.data.clfobj.ClfObjectOnceSorted
import lombok.Data
import lombok.NonNull
import lombok.RequiredArgsConstructor

import java.io.File
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays

import jb.util.ModelUtils.getIndexOfSubspace

class FileReaderNWay : FileHelper {

    @Throws(IOException::class, InvalidInputDataException::class)
    override fun readFile(opts: Opts): Dataset {

        /*val rootProblem = Problem.readFromFile(File(opts.filePath!!), opts.bias)
        val allObjects = getClfObjects(rootProblem)

        val numberOfSubsets = opts.numberOfBaseClassifiers + 2
        val countOfSubset = rootProblem.l / numberOfSubsets

        val trainingProblems = ArrayList<Problem>()
        val validatingProblems = ArrayList<Problem>()
        var testingProblem: Problem? = null

        val countOfValidatingObjects = IntArray(opts.numberOfSpaceParts)
        val clfObjectsOnceSorted = arrayOfNulls<ClfObjectOnceSorted>(countOfSubset)

        val extremeValues = ExtremeValues(allObjects).invoke()
        val xMin = extremeValues.getXMin()
        val xMax = extremeValues.getXMax()

        for (i in 0 until numberOfSubsets) {
            if (i != numberOfSubsets - 2) {
                val x = Array(countOfSubset) { DoubleArray(rootProblem.n) }
                val y = IntArray(countOfSubset)
                for (j in 0 until countOfSubset) {
                    x[j] = allObjects[j * numberOfSubsets + i].x
                    y[j] = allObjects[j * numberOfSubsets + i].y
                }
                val baseProblem = getBaseProblem(opts, rootProblem, countOfSubset, x, y)
                if (i == numberOfSubsets - 1) {
                    testingProblem = baseProblem
                } else {
                    trainingProblems.add(baseProblem)
                }
            } else {
                for (j in 0 until countOfSubset) {
                    countOfValidatingObjects[getIndexOfSubspace(opts.numberOfSpaceParts, allObjects[j * numberOfSubsets + 1].x[0], xMin, xMax)]++
                    clfObjectsOnceSorted[j] = allObjects[j * numberOfSubsets + i].convertToOnceSorted()
                }
            }
        }

        Arrays.sort(clfObjectsOnceSorted)
        var counter = 0

        for (i in countOfValidatingObjects.indices) {
            val x = Array(countOfValidatingObjects[i]) { DoubleArray(rootProblem.n) }
            val y = IntArray(countOfValidatingObjects[i])
            for (j in 0 until countOfValidatingObjects[i]) {
                val clfObjectDoubleSorted = clfObjectsOnceSorted[counter]
                x[j] = clfObjectDoubleSorted.getX()
                y[j] = clfObjectDoubleSorted.getY()
                counter++
            }
            val baseProblem = getBaseProblem(opts, rootProblem, countOfValidatingObjects[i], x, y)
            validatingProblems.add(baseProblem)
        }*/
        return Dataset(emptyList(), 0.0, 0.0)
    }

    private fun getBaseProblem(opts: Opts, problem: Problem, countOfSubset: Int, x: Array<DoubleArray>, y: IntArray): Problem {
        val baseProblem = Problem()
        baseProblem.bias = opts.bias
        //val features = Array<Array<Feature>>(countOfSubset) { arrayOfNulls(problem.n) }
        val features = null
        val labels = DoubleArray(countOfSubset)
        for (i in 0 until countOfSubset) {
            val baseFeatures = arrayOfNulls<Feature>(problem.n)
            for (j in 0 until problem.n) {
                baseFeatures[j] = FeatureNode(j, x[i][j])
            }
            labels[i] = y[i].toDouble()
        }
        baseProblem.x = features
        baseProblem.y = labels
        baseProblem.n = problem.n
        baseProblem.l = countOfSubset
        return baseProblem
    }

    private fun getClfObjects(problem: Problem): Array<ClfObjectDoubleSorted?> {
        val result = arrayOfNulls<ClfObjectDoubleSorted>(problem.l)
        for (i in 0 until problem.l) {
            val x = DoubleArray(problem.n)
            for (j in 0 until problem.n) {
                x[j] = problem.x[i][j].value
            }
            result[i] = ClfObjectDoubleSorted(x, problem.y[i].toInt())
        }
        Arrays.sort(result)
        return result
    }

    @Data
    @RequiredArgsConstructor
    private inner class ExtremeValues {
        @NonNull
        @get:lombok.NonNull
        var clfObjectsDoubleSorted: Array<ClfObjectDoubleSorted>? = null
            set(@lombok.NonNull clfObjectsDoubleSorted) {
                field = this.clfObjectsDoubleSorted
            }
        private var xMin: Double = 0.toDouble()
        private var xMax: Double = 0.toDouble()

        operator fun invoke(): ExtremeValues {
            var classChangeIndex = 0
            while (this.clfObjectsDoubleSorted!![classChangeIndex].y == 0) {
                classChangeIndex++
            }
            xMin = Math.min(this.clfObjectsDoubleSorted!![0].x[0], this.clfObjectsDoubleSorted!![classChangeIndex].x[0])
            xMax = Math.max(this.clfObjectsDoubleSorted!![this.clfObjectsDoubleSorted!!.size - 1].x[0], this.clfObjectsDoubleSorted!![classChangeIndex - 1].x[0])
            return this
        }
    }
}
