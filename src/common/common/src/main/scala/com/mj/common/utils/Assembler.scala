package com.mj.common.utils

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, SparkSession}

object Assembler {

  def apply(df: DataFrame)(implicit spark: SparkSession): DataFrame = {
    val assembler = new VectorAssembler()
      .setInputCols(Array("latitude", "longitude"))
      .setOutputCol("features")

    assembler.transform(df)
  }
}
