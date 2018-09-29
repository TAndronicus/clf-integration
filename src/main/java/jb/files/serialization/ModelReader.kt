package jb.files.serialization

import de.bwaldvogel.liblinear.Linear
import de.bwaldvogel.liblinear.Model
import jb.config.Opts

import java.io.File
import java.io.IOException
import java.util.ArrayList

import jb.config.Constants.modelsPath

class ModelReader {

    fun read(opts: Opts): List<Model> {
        val modelsDir = File(modelsPath)
        val clfs = ArrayList<Model>()
        for (file in modelsDir.listFiles()!!) {
            if (isRightData(opts, file) && hasRightAmountOfBaseClfs(opts, file) && !inPermutation(Integer.valueOf(file.name.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]), opts.permutation!!)) {
                try {
                    clfs.add(Linear.loadModel(file))
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return clfs
    }

    private fun inPermutation(numberOfSubset: Int, permutation: IntArray): Boolean {
        return numberOfSubset == permutation[0] || numberOfSubset == permutation[1]
    }

    private fun hasRightAmountOfBaseClfs(opts: Opts, file: File): Boolean {
        return Integer.valueOf(file.name.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[file.name.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]) == opts.numberOfBaseClassifiers + 2
    }

    private fun isRightData(opts: Opts, file: File): Boolean {
        return file.name.startsWith(opts.getFilename().substring(0, 3))
    }

}
