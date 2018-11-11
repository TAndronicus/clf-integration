package jb.runner

import org.apache.spark.ml.classification.LinearSVC
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.IndexToString
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.feature.VectorIndexerModel
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.StringIndexerModel
import org.apache.spark.*



fun main(args: Array<String>) {
    val session = SparkSession.builder().config("spark.master", "local").appName("Examp").orCreate
//    val dataset = session.read().format("libsvm").load("/home/jb/Downloads/1_0_1_converted.csv")
//    val linearSVC = LinearSVC().setMaxIter(10).setRegParam(.1)
//    val model = linearSVC.fit(dataset)
//    println(model.coefficients())

    // Load the data stored in LIBSVM format as a DataFrame.
    val data = session
            .read()
            .format("libsvm")
            .load("/home/jb/Downloads/test1.txt")

// Index labels, adding metadata to the label column.
// Fit on whole dataset to include all labels in index.
    val labelIndexer = StringIndexer()
            .setInputCol("label")
            .setOutputCol("indexedLabel")
            .fit(data)

// Automatically identify categorical features, and index them.
    val featureIndexer = VectorIndexer()
            .setInputCol("features")
            .setOutputCol("indexedFeatures")
            .setMaxCategories(4) // features with > 4 distinct values are treated as continuous.
            .fit(data)

// Split the data into training and test sets (30% held out for testing).
    val splits = data.randomSplit(doubleArrayOf(0.7, 0.3))
    val trainingData = splits[0]
    val testData = splits[1]

// Train a DecisionTree model.
    val dt = DecisionTreeClassifier()
            .setLabelCol("indexedLabel")
            .setFeaturesCol("indexedFeatures")

// Convert indexed labels back to original labels.
    val labelConverter = IndexToString()
            .setInputCol("prediction")
            .setOutputCol("predictedLabel")
            .setLabels(labelIndexer.labels())

// Chain indexers and tree in a Pipeline.
    val pipeline = Pipeline()
            .setStages(arrayOf(labelIndexer, featureIndexer, dt, labelConverter))

// Train model. This also runs the indexers.
    val model = pipeline.fit(data)

// Make predictions.
    val predictions = model.transform(testData)

// Select example rows to display.
    predictions.select("predictedLabel", "label", "features").show(5)

// Select (prediction, true label) and compute test error.
    val evaluator = MulticlassClassificationEvaluator()
            .setLabelCol("indexedLabel")
            .setPredictionCol("prediction")
            .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println("Test Error = " + (1.0 - accuracy))

    val treeModel = model.stages()[2] as DecisionTreeClassificationModel
    println("Learned classification tree model:\n" + treeModel.toDebugString())

}
