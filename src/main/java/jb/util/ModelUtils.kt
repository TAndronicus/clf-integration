package jb.util

import de.bwaldvogel.liblinear.Feature
import de.bwaldvogel.liblinear.FeatureNode
import de.bwaldvogel.liblinear.Linear
import de.bwaldvogel.liblinear.Model
import jb.config.Constants.Companion.EPSILON
import jb.config.Opts
import jb.data.clfobj.ClfObject

import java.util.ArrayList
import java.util.Arrays

object ModelUtils {

    fun getAs(clfs: List<Model>): DoubleArray {
        val `as` = DoubleArray(clfs.size)
        for (i in clfs.indices) {
            `as`[i] = -clfs[i].featureWeights[0] / clfs[i].featureWeights[1]
        }
        return `as`
    }

    fun getBs(clfs: List<Model>): DoubleArray {
        val bs = DoubleArray(clfs.size)
        for (i in clfs.indices) {
            bs[i] = -clfs[i].featureWeights[2] / clfs[i].featureWeights[1]
        }
        return bs
    }

    fun predictsPropperly(model: Model, clfObject: ClfObject): Boolean {
        val features = arrayOfNulls<Feature>(clfObject.x.size)
        for (i in 0 until clfObject.x.size) {
            features[i] = FeatureNode(i + 1, clfObject.x[i])
        }
        val prediction = Linear.predict(model, features)
        return prediction == clfObject.y.toDouble()
    }

    fun getIndexOfSubspace(numberOfSpaceParts: Int, xSample: Double, minX: Double, maxX: Double): Int {
        return (numberOfSpaceParts * (xSample - minX) / (maxX - minX) - EPSILON).toInt()
    }

    fun getIndexOfSubspace(x: DoubleArray, sample: Double): Int {
        var ind = Arrays.binarySearch(x, sample)
        ind = if (ind < 0) -2 - ind else ind - 1
        ind = if (ind == -1) 0 else ind
        return ind
    }

    fun calculateScoreFromConfMat(confMat: Array<IntArray>): Double {
        return (.0 + confMat[0][0].toDouble() + confMat[1][1].toDouble()) / (confMat[0][0] + confMat[0][1] + confMat[1][0] + confMat[1][1])
    }

    fun switchConfMatColumns(confusionMatrix: Array<IntArray>) {
        for (i in confusionMatrix.indices) {
            val temp = confusionMatrix[i][0]
            confusionMatrix[i][0] = confusionMatrix[i][1]
            confusionMatrix[i][1] = temp
        }
    }

    fun calculateMccFromConfMat(confMat: Array<IntArray>): Double {
        val nominator = .0 + confMat[0][0] * confMat[1][1] - confMat[0][1] * confMat[1][0]
        var denominatorSq = 1.0
        for (i in 0..1) {
            for (j in 0..1) {
                denominatorSq *= (confMat[i][i] + confMat[j][1 - j]).toDouble()
            }
        }
        val denominator = Math.sqrt(denominatorSq)
        return if (denominator == 0.0) 0.0 else nominator / denominator
    }

    fun calculateRelativeValue(absoluteValue: Double, minValue: Double, maxValue: Double): Double {
        return (absoluteValue - minValue) / (maxValue - minValue)
    }

    fun pickModels(clfs: List<Model>, opts: Opts): List<Model> {
        val pickedModels = ArrayList<Model>()
        for (i in clfs.indices)
            if (i != opts.permutation[0] && i != opts.permutation[1]) pickedModels.add(clfs[i])
        return pickedModels
    }

    fun pickModels(clfs: List<Model>, indices: IntArray): List<Model> {
        val selectedClfs = ArrayList<Model>()
        for (index in indices) {
            selectedClfs.add(clfs[index])
        }
        return selectedClfs
    }

}
