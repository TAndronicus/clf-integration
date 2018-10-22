package jb.trainer

import de.bwaldvogel.liblinear.Linear
import de.bwaldvogel.liblinear.Model
import de.bwaldvogel.liblinear.Parameter
import jb.config.Opts
import jb.data.Dataset

import java.util.ArrayList

class SvmTrainer : Trainer {

    override fun train(dataset: Dataset, opts: Opts): List<Model> {
        val parameter = Parameter(opts.solverType, opts.c, opts.eps)
        val clfs = ArrayList<Model>()
        for (problem in dataset.problems) {
            clfs.add(Linear.train(problem.convertToLibLinearProblem(opts), parameter))
        }
        return clfs
    }

}
