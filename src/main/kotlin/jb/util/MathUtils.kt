package jb.util

import java.util.ArrayList

object MathUtils {

    fun vectorProduct(v1: DoubleArray, v2: DoubleArray): Double {
        var product = 0.0
        for (i in v1.indices) {
            product += v1[i] * v2[i]
        }
        return product
    }

    fun vectorTrace(v1: DoubleArray): Double {
        var trace = 0.0
        for (v in v1) {
            trace += v
        }
        return trace
    }

    fun getCombinationsOfTwo(range: Int): List<IntArray> {
        val combinations = ArrayList<IntArray>()
        for (i in 0 until range) {
            for (j in 0 until range) {
                if (i != j) combinations.add(intArrayOf(i, j))
            }
        }
        return combinations
    }

    fun arePermutations(v0: IntArray, v1: IntArray): Boolean {
        return if (v0.size == 1) v0[0] == v1[0] else v0[0] == v1[0] && v0[1] == v1[1] || v0[0] == v1[1] && v0[1] == v1[0]
    }

}
