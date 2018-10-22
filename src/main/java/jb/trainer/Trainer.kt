package jb.trainer

import de.bwaldvogel.liblinear.Model
import jb.config.Opts
import jb.data.Dataset

interface Trainer {

    fun train(dataset: Dataset, opts: Opts): List<Model>

}
