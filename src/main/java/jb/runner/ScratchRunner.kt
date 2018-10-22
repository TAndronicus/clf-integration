package jb.runner

import de.bwaldvogel.liblinear.InvalidInputDataException
import de.bwaldvogel.liblinear.SolverType
import jb.config.Opts
import jb.files.BReader
import jb.files.FileHelper
import jb.integrator.Integrator
import jb.integrator.MeanIntegrator
import jb.selector.NBestSelector
import jb.selector.Selector
import jb.tester.IntegratedScoreTester
import jb.trainer.SvmTrainer
import jb.trainer.Trainer
import jb.util.BUtils
import jb.util.MathUtils
import jb.validator.SimpleScoreValidator
import jb.validator.Validator
import java.util.*

private var fileHelper: FileHelper = BReader()
private var trainer: Trainer = SvmTrainer()
private var validator: Validator = SimpleScoreValidator()
private var selector: Selector = NBestSelector()
private var integrator: Integrator = MeanIntegrator()

fun main(args: Array<String>) {


    val combinations = MathUtils.getCombinationsOfTwo(11)
    val finalScores = TreeSet<Double>()
    for (combination in combinations) {
        val permutations = BUtils.getAllPermutations()
        val scores = TreeMap<Double, IntArray>()
        for (permutation in permutations) {
            val opts = Opts(filePath = "src/main/resources/target/7_" + permutation[0] + "_" + permutation[1] + "_converted.csv",
                    bias = 1.0,
                    numberOfBaseClassifiers = 9,
                    numberOfSelectedClassifiers = 3,
                    numberOfSpaceParts = 3,
                    solverType = SolverType.L2R_LR,
                    c = 1.0,
                    eps = .01,
                    permutation = combination)
            val dataset = fileHelper.readFile(opts)
            val clfs = trainer.train(dataset, opts)
            val validatingTestingTuple = dataset.getValidatingTestingTuple(opts)
            val scoreTuple = validator.validate(clfs, validatingTestingTuple, opts)
            val selectedTuple = selector.select(scoreTuple, opts)
            val integratedModel = integrator.integrate(selectedTuple, clfs, dataset, opts)
            val integratedScoreTester = IntegratedScoreTester()
            scores[integratedScoreTester.test(integratedModel, validatingTestingTuple)] = permutation
        }
        finalScores.add(scores.lastKey())
    }
    var sum = 0.0
    for (finalScore in finalScores) {
        sum += finalScore
    }
    println(sum / finalScores.size)
    println(finalScores.last())

}