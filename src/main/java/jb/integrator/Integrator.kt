package jb.integrator

import de.bwaldvogel.liblinear.Model
import jb.config.Opts
import jb.data.Dataset
import jb.data.IntegratedModel
import jb.data.SelectedTuple

interface Integrator {

    fun integrate(selectedTuple: SelectedTuple, clfs: List<Model>, dataset: Dataset, opts: Opts): IntegratedModel

}
