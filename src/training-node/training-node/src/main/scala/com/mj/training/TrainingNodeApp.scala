package com.mj.training

import com.mj.common.settings.Settings._
import com.mj.common.utils._
import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.ml.clustering.KMeansModel
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.sql.DataFrame

object TrainingNodeApp extends SparkJob with StrictLogging {

  def main(args: Array[String]): Unit = {
    val trainingPath = appSettings.inputPath
    //model path
    val dataModelPath = appSettings.modelPath

    //todo: calculate culster
    val clusters = 2
    val iterators = 20
    val seed = 1L

    val inputDf: DataFrame = Reader(trainingPath)

    val workingDf: DataFrame = Transformer(inputDf)

    val assemblerDf: DataFrame = Assembler(workingDf)

    val model: KMeansModel = Modeler(clusters, iterators, seed, assemblerDf)

    // Make predictions
    val predictions = model.transform(assemblerDf)

    // Evaluate clustering by computing Silhouette score
    val evaluator = new ClusteringEvaluator()

    val silhouette = evaluator.evaluate(predictions)

    println(s"Silhouette with squared euclidean distance = $silhouette")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    // Save the model
    model.write.overwrite.save(dataModelPath)

    spark.stop()
  }

}
