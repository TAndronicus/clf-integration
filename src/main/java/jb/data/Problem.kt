package jb.data

import de.bwaldvogel.liblinear.Feature
import de.bwaldvogel.liblinear.FeatureNode
import jb.config.Opts

data class Problem(var x: Array<DoubleArray>, var y: IntArray) {

    fun convertToLibLinearProblem(opts: Opts): de.bwaldvogel.liblinear.Problem {
        val problem = de.bwaldvogel.liblinear.Problem()
        problem.bias = opts.bias
        problem.l = this.x.size
        problem.n = this.x[0].size
        val features = Array(x.size) { _ -> Array<Feature>(x[0].size) { _ -> FeatureNode(0, 0.0) } }
        val classes = DoubleArray(problem.l)
        for (i in 0 until problem.l) {
            val baseFeature: Array<Feature> = Array(problem.n) { _ -> FeatureNode(0, 0.0) }
            for (j in 0 until problem.n) {
                baseFeature[j] = FeatureNode(j + 1, this.x[i][j])
            }
            features[i] = baseFeature
            classes[i] = this.y[i].toDouble()
        }
        problem.x = features
        problem.y = classes
        return problem
    }

    fun getObjectAsFeatures(index: Int): Array<Feature> {
        val features: Array<Feature> = Array(x.size) { _ -> FeatureNode(0, 0.0) }
        for (i in 0 until this.x[index].size) {
            features[i] = FeatureNode(i + 1, this.x[index][i])
        }
        return features
    }

}
