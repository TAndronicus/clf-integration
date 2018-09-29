package jb.integrator

import de.bwaldvogel.liblinear.Model
import jb.config.Opts
import jb.data.Dataset
import jb.data.FunctionValue
import jb.data.IntegratedModel
import jb.data.SelectedTuple


import jb.util.MathUtils.arePermutations
import jb.util.ModelUtils.*

class MedianIntegrator : Integrator {
    override fun integrate(selectedTuple: SelectedTuple, clfs: List<Model>, dataset: Dataset, opts: Opts): IntegratedModel {

        val (a, b, x) = getDuplicatedModel(selectedTuple, clfs, dataset, opts)
        val resultAs = ArrayList<Double>()
        val resultBs = ArrayList<Double>()
        val resultX = ArrayList<Double>()
        resultAs.add(a[0])
        resultBs.add(b[0])
        resultX.add(x[0])
        for (i in 0 until x.size - 2) {
            if (a[i] == a[i + 1] && b[i] == b[i + 1]) continue
            resultAs.add(a[i + 1])
            resultBs.add(b[i + 1])
            resultX.add(x[i + 1])
        }
        resultX.add(x[x.size - 1])
        /*return IntegratedModel(
                resultAs.stream().mapToDouble(ToDoubleFunction<Double> { it.toDouble() }).toArray(),
                resultBs.stream().mapToDouble(ToDoubleFunction<Double> { it.toDouble() }).toArray(),
                resultX.stream().mapToDouble(ToDoubleFunction<Double> { it.toDouble() }).toArray())*/
        return IntegratedModel(resultAs.toDoubleArray(), resultBs.toDoubleArray(), resultX.toDoubleArray())
    }

    private fun getDuplicatedModel(selectedTuple: SelectedTuple, clfs: List<Model>, dataset: Dataset, opts: Opts): IntegratedModel {
        val duplicatingAs = ArrayList<Double>()
        val duplicatingBs = ArrayList<Double>()
        val duplicatingX = ArrayList<Double>()
        for (i in 0 until opts.numberOfSpaceParts) {
            val selectedClfs = pickModels(clfs, selectedTuple.indices[i])
            val `as` = getAs(selectedClfs)
            val bs = getBs(selectedClfs)
            val crosspoints = getAllCrosspoints(`as`, bs)
            val filteredCrosspoints = filterCrosspoints(crosspoints, dataset, i, opts)
            filteredCrosspoints.sorted()
            var previousIndices: IntArray? = null
            for (j in 0 until filteredCrosspoints.size - 1) {
                val indices = getMiddleIndices(`as`, bs, filteredCrosspoints, j)
                if (sameIndices(previousIndices, indices)) continue
                duplicatingAs.add(if (indices.size == 1) `as`[indices[0]] else (`as`[indices[0]] + `as`[indices[1]]) / 2)
                duplicatingBs.add(if (indices.size == 1) bs[indices[0]] else (bs[indices[0]] + bs[indices[1]]) / 2)
                duplicatingX.add(filteredCrosspoints[j])
                previousIndices = indices
            }
        }
        duplicatingX.add(dataset.maxX)
        return IntegratedModel(duplicatingAs.toDoubleArray(), duplicatingBs.toDoubleArray(), duplicatingX.toDoubleArray())
    }

    private fun getMiddleIndices(`as`: DoubleArray, bs: DoubleArray, filteredCrosspoints: List<Double>, j: Int): IntArray {
        val functionValues = ArrayList<FunctionValue>()
        for (k in `as`.indices) {
            functionValues.add(FunctionValue(k, `as`[k] * (filteredCrosspoints[j] + filteredCrosspoints[j + 1]) / 2 + bs[k]))
        }
        functionValues.sortedBy { it.value }
        return getMiddleIndices(functionValues)
    }

    private fun sameIndices(previousIndices: IntArray?, indices: IntArray): Boolean {
        return previousIndices != null && previousIndices.size == indices.size && arePermutations(previousIndices, indices)
    }

    private fun getMiddleIndices(functionValues: List<FunctionValue>): IntArray {
        return if (functionValues.size % 2 == 0)
            intArrayOf(functionValues[functionValues.size / 2 - 1].index, functionValues[functionValues.size / 2].index)
        else
            intArrayOf(functionValues[(functionValues.size - 1) / 2].index)
    }

    private fun getAllCrosspoints(`as`: DoubleArray, bs: DoubleArray): List<Double> {
        val x = ArrayList<Double>()
        for (i in `as`.indices) {
            for (j in bs.indices) {
                if (i == j || `as`[i] == `as`[j]) continue
                x.add((bs[i] - bs[j]) / (`as`[i] - `as`[j]))
            }
        }
        return x
    }

    private fun filterCrosspoints(crosspoints: List<Double>, dataset: Dataset, index: Int, opts: Opts): List<Double> {
        val filteredCrosspoints = ArrayList<Double>()
        filteredCrosspoints.add(getLowerLimit(dataset, index, opts))
        filteredCrosspoints.add(getHigherLimit(dataset, index, opts))
        for (crosspoint in crosspoints) {
            if (crosspoint < getLowerLimit(dataset, index, opts) || crosspoint > getHigherLimit(dataset, index, opts) || filteredCrosspoints.contains(crosspoint))
                continue
            filteredCrosspoints.add(crosspoint)
        }
        return filteredCrosspoints
    }

    private fun getLowerLimit(dataset: Dataset, index: Int, opts: Opts): Double {
        return dataset.minX + index * (dataset.maxX - dataset.minX) / opts.numberOfSpaceParts
    }

    private fun getHigherLimit(dataset: Dataset, index: Int, opts: Opts): Double {
        return dataset.minX + (index + 1) * (dataset.maxX - dataset.minX) / opts.numberOfSpaceParts
    }

}
