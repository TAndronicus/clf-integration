package jb.validator

import de.bwaldvogel.liblinear.Model
import jb.config.Opts
import jb.data.Problem
import jb.data.ScoreTuple
import jb.data.ValidatingTestingTuple
import jb.data.clfobj.ClfObjectOnceSorted

import jb.util.ModelUtils.predictsPropperly


class SimpleScoreValidator : Validator {

    override fun validate(clfs: List<Model>, validatingTestingTuple: ValidatingTestingTuple, opts: Opts): ScoreTuple {

        val scores = Array(clfs.size) { DoubleArray(opts.numberOfSpaceParts) }
        val weights = Array(opts.numberOfSpaceParts) { DoubleArray(clfs.size) }


        for (i in clfs.indices) {
            for (j in 0 until opts.numberOfSpaceParts) {
                var propperlyClassified = 0.0
                val (x, y) = validatingTestingTuple.validationProblems[j]
                for (k in 0 until x.size) {
                    if (predictsPropperly(clfs[i], ClfObjectOnceSorted(x[k], y[k]))) {
                        propperlyClassified++
                    }
                }
                scores[i][j] = propperlyClassified / validatingTestingTuple.validationProblems[j].y.size
                weights[j][i] = propperlyClassified / validatingTestingTuple.validationProblems[j].y.size
            }
        }

        return ScoreTuple(scores, weights)
    }

}
