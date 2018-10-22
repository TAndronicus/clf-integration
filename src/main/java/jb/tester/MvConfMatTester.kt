package jb.tester

import de.bwaldvogel.liblinear.Linear
import de.bwaldvogel.liblinear.Model
import jb.config.Opts
import jb.data.Problem
import jb.data.ValidatingTestingTuple
import jb.util.ModelUtils

class MvConfMatTester {

    fun test(clfs: List<Model>, validatingTestingTuple: ValidatingTestingTuple): Array<IntArray> {
        val confMat = arrayOf(intArrayOf(0, 0), intArrayOf(0, 0))
        var realIndex: Int
        var predictedIndex: Int
        val problem = validatingTestingTuple.testingProblem
        for (i in 0 until problem.y.size) {
            realIndex = if (problem.y[i] == 1) 1 else 0
            var votingResult = 0.0
            for (model in clfs) {
                votingResult += Linear.predict(model, problem.getObjectAsFeatures(i))
            }
            predictedIndex = if (votingResult / clfs.size > .5) 1 else 0
            confMat[realIndex][predictedIndex]++
        }
        if (ModelUtils.calculateScoreFromConfMat(confMat) < .5) ModelUtils.switchConfMatColumns(confMat)
        return confMat
    }

}
