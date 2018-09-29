package jb.files

import de.bwaldvogel.liblinear.InvalidInputDataException
import de.bwaldvogel.liblinear.Problem
import jb.config.Opts
import jb.data.Dataset
import jb.data.clfobj.ClfObject
import jb.data.clfobj.ClfObjectDoubleSorted

import java.io.File
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays

class FileReaderNWaySameValidation : FileHelper {

    @Throws(IOException::class, InvalidInputDataException::class)
    override fun readFile(opts: Opts): Dataset {

        val problem = Problem.readFromFile(File(opts.filePath!!), opts.bias)
        val clfObjectDoubleSorteds = arrayOfNulls<ClfObjectDoubleSorted>(problem.l)
        for (i in 0 until problem.l) {
            val x = DoubleArray(problem.n)
            for (j in 0 until problem.n) {
                x[j] = problem.x[i][j].value
            }
            clfObjectDoubleSorteds[i] = ClfObjectDoubleSorted(x, problem.y[i].toInt())
        }
        Arrays.sort(clfObjectDoubleSorteds)

        val numberOfSubsets = opts.numberOfBaseClassifiers + 2
        val countOfSubset = problem.l / numberOfSubsets
        val problems = ArrayList<jb.data.Problem>()
        for (i in 0 until numberOfSubsets) {
            val x = Array(countOfSubset) { DoubleArray(problem.n) }
            val y = IntArray(countOfSubset)
            for (j in 0 until countOfSubset) {
                x[j] = clfObjectDoubleSorteds[j * numberOfSubsets + i]!!.x
                y[j] = clfObjectDoubleSorteds[j * numberOfSubsets + i]!!.y
            }
            problems.add(jb.data.Problem(x, y))
        }
        return Dataset(problems, clfObjectDoubleSorteds[0]!!.x[0], clfObjectDoubleSorteds[clfObjectDoubleSorteds.size - 1]!!.x[0])
    }

}
