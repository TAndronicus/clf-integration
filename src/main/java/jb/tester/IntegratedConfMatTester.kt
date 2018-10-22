package jb.tester

import jb.config.Opts
import jb.data.IntegratedModel
import jb.data.ValidatingTestingTuple
import jb.util.ModelUtils
import lombok.Data

import jb.util.ModelUtils.getIndexOfSubspace
import jb.util.ModelUtils.switchConfMatColumns

class IntegratedConfMatTester {

    fun test(integratedModel: IntegratedModel, validatingTestingTuple: ValidatingTestingTuple, opts: Opts): Array<IntArray> {
        val confusionMatrix = arrayOf(intArrayOf(0, 0), intArrayOf(0, 0))
        var realIndex: Int
        var predictedIndex: Int
        for (i in 0 until validatingTestingTuple.testingProblem.y.size) {
            val index = getIndexOfSubspace(integratedModel.x, validatingTestingTuple.testingProblem.x[i][0])
            val value = integratedModel.a[index] * validatingTestingTuple.testingProblem.x[i][0] + integratedModel.b[index]
            realIndex = if (validatingTestingTuple.testingProblem.y[i] == 1) 1 else 0
            predictedIndex = if (value > validatingTestingTuple.testingProblem.x[i][1]) 1 else 0
            confusionMatrix[realIndex][predictedIndex]++
        }
        if (ModelUtils.calculateScoreFromConfMat(confusionMatrix) < .5) {
            switchConfMatColumns(confusionMatrix)
        }
        return confusionMatrix
    }


}
