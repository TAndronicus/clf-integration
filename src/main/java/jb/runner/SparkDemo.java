package jb.runner;

import org.apache.spark.SparkConf;
import org.apache.spark.ml.classification.LinearSVC;
import org.apache.spark.ml.classification.LinearSVCModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.concurrent.TimeUnit;

public class SparkDemo {

    public static void main(String[] args) throws InterruptedException {

        SparkSession session = SparkSession.builder().config("spark.master", "local").appName("LinSvcExample").getOrCreate();
        Dataset<Row> dataset = session.read().format("libsvm").load("/home/jb/Downloads/1_0_1_converted.csv");
        LinearSVC linearSVC = new LinearSVC().setMaxIter(10).setRegParam(0.1);
        LinearSVCModel model = linearSVC.fit(dataset);
        System.out.println(model.coefficients());
        TimeUnit.MINUTES.sleep(10);

    }

}
