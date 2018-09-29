package jb.files

import de.bwaldvogel.liblinear.InvalidInputDataException
import jb.config.Opts
import jb.data.Dataset

import java.io.IOException

@FunctionalInterface
interface FileHelper {

    @Throws(IOException::class, InvalidInputDataException::class)
    fun readFile(opts: Opts): Dataset

}
