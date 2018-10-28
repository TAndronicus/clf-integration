package jb.runner

import org.apache.spark.ml.classification.LinearSVC
import org.apache.spark.sql.SparkSession

fun main(args: Array<String>) {
    val session = SparkSession.builder().config("spark.master", "local").appName("Examp").orCreate
    val dataset = session.read().format("libsvm").load("/home/jb/Downloads/1_0_1_converted.csv")
    val linearSVC = LinearSVC().setMaxIter(10).setRegParam(.1)
    val model = linearSVC.fit(dataset)
    println(model.coefficients())

}