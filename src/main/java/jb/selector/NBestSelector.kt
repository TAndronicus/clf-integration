package jb.selector

import jb.config.Opts
import jb.data.ScoreTuple
import jb.data.SelectedTuple

import java.util.Arrays

class NBestSelector : Selector {

    override fun select(validationResults: ScoreTuple, opts: Opts): SelectedTuple {

        val indices = Array(opts.numberOfSpaceParts) { IntArray(opts.numberOfSelectedClassifiers) }
        val weights = Array(opts.numberOfSpaceParts) { DoubleArray(opts.numberOfSelectedClassifiers) }
        if (opts.numberOfBaseClassifiers == opts.numberOfSelectedClassifiers) {
            for (i in 0 until opts.numberOfSpaceParts) {
                for (j in 0 until opts.numberOfSelectedClassifiers) {
                    indices[i][j] = j
                }
            }
            return SelectedTuple(indices, validationResults.weights)
        }
        for (i in 0 until opts.numberOfSpaceParts) {
            val weightsProSubspace = validationResults.weights[i]
            val copyOfWeightsProSubspace = Arrays.copyOf(weightsProSubspace, weightsProSubspace.size)
            Arrays.sort(copyOfWeightsProSubspace)
            val cutCopyOfWeightsProSubspace = Arrays.copyOfRange(copyOfWeightsProSubspace, opts.numberOfBaseClassifiers - opts.numberOfSelectedClassifiers, copyOfWeightsProSubspace.size)
            var counter = 0
            for (j in 0 until opts.numberOfBaseClassifiers) {
                if (counter == opts.numberOfSelectedClassifiers) {
                    break
                }
                if (Arrays.binarySearch(cutCopyOfWeightsProSubspace, weightsProSubspace[j]) >= 0) {
                    indices[i][counter] = j
                    weights[i][counter] = weightsProSubspace[j]
                    counter++
                }
            }
        }
        return SelectedTuple(indices, weights)
    }

}
