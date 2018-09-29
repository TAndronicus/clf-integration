package jb.config

import de.bwaldvogel.liblinear.SolverType

data class Opts(val filePath: String?, //Absolute path to file in LibSVM format
                val bias: Double, //Non-positive bias means fixing classifier discriminator function in (0, 0, ..., 0)
                val numberOfBaseClassifiers: Int, //Number of base classifiers
                val numberOfSelectedClassifiers: Int, // Number of selected classifiers
                val numberOfSpaceParts: Int, // Number of space parts
                val solverType: SolverType?, // Solver type
                val c: Double, // Cost of constraints violation
                val eps: Double, // Stopping criteria
                val permutation: IntArray? // [i, j], where i - index of validation dataset, j - index of testing dataset
) {

    fun getFilename(): String {
            val pathParts = this.filePath!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val nameParts = pathParts[pathParts.size - 1].split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return nameParts[0]
    }

}
