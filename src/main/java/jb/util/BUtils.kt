package jb.util

import java.util.ArrayList

class BUtils {

    companion object {
        fun getAllPermutations(): List<IntArray> {
            val permutations = ArrayList<IntArray>()
            for (i in 0..6) {
                for (j in i + 1..7) {
                    permutations.add(intArrayOf(i, j))
                }
            }
            return permutations
        }
    }
}
