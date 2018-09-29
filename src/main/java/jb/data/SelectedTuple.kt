package jb.data

data class SelectedTuple(
        /**
         * indices[numberOfSpaceParts][numberOfSelectedClassifiers]
         */
        var indices: Array<IntArray>,
        /**
         * weights[numberOfSpaceParts][numberOfSelectedClassifiers]
         */
        var weights: Array<DoubleArray>)
