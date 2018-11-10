package jb.data

class ScoreTuple(var scores: Array<DoubleArray>, // scores[numberOfBaseClassifiers][numberOfSpaceParts]
                 var weights: Array<DoubleArray>) // weight[numberOfSpaceParts][numberOfBaseClassifiers]
