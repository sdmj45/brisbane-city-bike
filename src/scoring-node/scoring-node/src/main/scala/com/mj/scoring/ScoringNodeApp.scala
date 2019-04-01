package com.mj.scoring

import com.mj.common.settings.Settings.appSettings
import com.mj.common.utils._
import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.ml.clustering.KMeansModel
import org.apache.spark.sql.{DataFrame, SaveMode}

object ScoringNodeApp extends SparkJob with StrictLogging {

  def main(args: Array[String]): Unit = {
    //input path
    val trainingPath = appSettings.inputPath
    //model path
    val dataModelPath = appSettings.modelPath
    //output path
    val dataOutputPath = appSettings.outputPath

    val inputDf: DataFrame = Reader(trainingPath)
    println("inputDf")
    inputDf.show(false)

    val workingDf: DataFrame = Transformer(inputDf)
    println("workingDf")
    workingDf.show(false)

    val assemblerDf: DataFrame = Assembler(workingDf)
    println("assemblerDf")
    assemblerDf.show()

    val model = KMeansModel.load(dataModelPath)

    // Join with input data before saving
    model
      .transform(assemblerDf)
      .selectExpr(Seq("id", "prediction"): _*)
      .join(workingDf, Seq("id"))
      .repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .json(dataOutputPath)

    spark.stop()
  }

}
