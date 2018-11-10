package jb.integrator

import de.bwaldvogel.liblinear.Model
import jb.config.Opts
import jb.data.Dataset
import jb.data.IntegratedModel
import jb.data.SelectedTuple
import jb.util.MathUtils

import java.util.ArrayList
import java.util.Arrays

import jb.util.ModelUtils.getAs
import jb.util.ModelUtils.getBs

class MeanIntegrator : Integrator {

    override fun integrate(selectedTuple: SelectedTuple, clfs: List<Model>, dataset: Dataset, opts: Opts): IntegratedModel {

        val a = DoubleArray(opts.numberOfSpaceParts)
        val b = DoubleArray(opts.numberOfSpaceParts)
        val x = DoubleArray(opts.numberOfSpaceParts + 1)
        x[0] = dataset.minX
        x[opts.numberOfSpaceParts] = dataset.maxX
        for (i in 0 until opts.numberOfSpaceParts) {
            val selectedClfs = getSelectedClfs(clfs, selectedTuple.indices[i])
            val az = getAs(selectedClfs)
            val bs = getBs(selectedClfs)
            a[i] = MathUtils.vectorProduct(az, selectedTuple.weights[i]) / MathUtils.vectorTrace(selectedTuple.weights[i])
            b[i] = MathUtils.vectorProduct(bs, selectedTuple.weights[i]) / MathUtils.vectorTrace(selectedTuple.weights[i])
            if (i != opts.numberOfSpaceParts - 1)
                x[i + 1] = dataset.minX + (i + 1) * (dataset.maxX - dataset.minX) / opts.numberOfSpaceParts
        }
        return IntegratedModel(a, b, x)
    }

    private fun getSelectedClfs(clfs: List<Model>, index: IntArray): List<Model> {
        val selectedClfs = ArrayList<Model>(index.size)
        for (i in clfs.indices) {
            if (Arrays.binarySearch(index, i) >= 0) {
                selectedClfs.add(clfs[i])
            }
        }
        return selectedClfs
    }

}
