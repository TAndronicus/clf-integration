package jb.tester

import de.bwaldvogel.liblinear.Linear
import de.bwaldvogel.liblinear.Model
import jb.data.Problem
import jb.data.ValidatingTestingTuple

class MVScoreTester {

    fun test(clfs: List<Model>, validatingTestingTuple: ValidatingTestingTuple): Double {
        var score = 0.0
        val problem = validatingTestingTuple.testingProblem
        for (i in 0 until problem.y.size) {
            var votingResult = 0.0
            for (model in clfs) {
                votingResult += Linear.predict(model, problem.getObjectAsFeatures(i))
            }
            if ((votingResult / clfs.size > .5) xor (problem.y[i] == 1)) {
                score++
            }
        }
        score /= problem.y.size.toDouble()
        return if (score > .5) score else 1 - score
    }
}
