package jb.data

import java.util.*

data class IntegratedModel(var a: DoubleArray, var b: DoubleArray,
                      /**
                       * Points between which the discriminant limit has the same function
                       * Span                | a        | b
                       * minX - x[0]         | a[0]     | b[0]
                       * x[0] - x[1]         | a[1]     | b[1]
                       * x[1] - x[2]         | a[2]     | b[2]
                       * ...                 | ...      | ...
                       * x[n - 2] - x[n - 1] | a[n - 1] | b[n - 1]
                       * x[n - 1] - maxX     | a[n]     | n[n]
                       */
                      var x: DoubleArray) {

    // boilerplate

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntegratedModel

        if (!Arrays.equals(a, other.a)) return false
        if (!Arrays.equals(b, other.b)) return false
        if (!Arrays.equals(x, other.x)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(a)
        result = 31 * result + Arrays.hashCode(b)
        result = 31 * result + Arrays.hashCode(x)
        return result
    }
}
