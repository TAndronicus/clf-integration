package jb.files.serialization

import de.bwaldvogel.liblinear.Linear
import de.bwaldvogel.liblinear.Model
import jb.config.Constants.Companion.modelsPath
import jb.config.Opts

import java.io.File
import java.io.IOException

class ModelWriter {

    fun saveModels(clfs: List<Model>, opts: Opts) {

        for (i in clfs.indices) {
            try {
                val root = File(modelsPath)
                if (!root.exists()) root.mkdir()
                Linear.saveModel(File(modelsPath + "/" + opts.getFilename().substring(0, 3) + "_" + i + "_on_" + clfs.size), clfs[i])
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

}
