package jb.tester

import jb.config.Opts
import jb.data.IntegratedModel
import jb.data.Problem
import jb.data.ValidatingTestingTuple

import jb.util.ModelUtils.getIndexOfSubspace

class IntegratedScoreTester {

    fun test(integratedModel: IntegratedModel, validatingTestingTuple: ValidatingTestingTuple): Double {
        var counter = 0.0
        val (x, y) = validatingTestingTuple.testingProblem
        for (i in 0 until y.size) {
            val index = getIndexOfSubspace(integratedModel.x, x[i][0])
            val value = integratedModel.a[index] * x[i][0] + integratedModel.b[index]
            if ((value > x[i][1]) xor (y[i] == 1)) {
                counter++
            }
        }
        var score = counter / y.size
        score = if (score > .5) score else 1 - score
        return score
    }
}
