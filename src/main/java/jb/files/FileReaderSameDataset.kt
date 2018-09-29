package jb.files

import de.bwaldvogel.liblinear.InvalidInputDataException
import de.bwaldvogel.liblinear.Problem
import jb.config.Opts
import jb.data.Dataset

import java.io.File
import java.io.IOException
import java.util.ArrayList

class FileReaderSameDataset : FileHelper {

    @Throws(IOException::class, InvalidInputDataException::class)
    override fun readFile(opts: Opts): Dataset {
        val problem = Problem.readFromFile(File(opts.filePath!!), 1.0)
        val trainingProblems = ArrayList<Problem>()
        for (i in 0 until opts.numberOfBaseClassifiers) {
            trainingProblems.add(problem)
        }
        val validatingProblems = ArrayList<Problem>()
        for (i in 0 until opts.numberOfSpaceParts) {
            validatingProblems.add(problem)
        }
        var minX = problem.x[0][0].value
        var maxX = minX
        for (i in 1 until problem.x.size) {
            minX = Math.min(minX, problem.x[i][0].value)
            maxX = Math.max(maxX, problem.x[i][0].value)
        }
        return Dataset(emptyList(), 0.0, 0.0)
    }

}
