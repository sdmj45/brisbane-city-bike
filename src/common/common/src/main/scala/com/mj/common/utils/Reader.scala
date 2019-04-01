package com.mj.common.utils

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Reading from input path to generate DataFrame
  */
object Reader {

  def apply(path: String)(implicit spark: SparkSession): DataFrame = {
    spark.read
      .option("multiLine", value = true)
      .json(path)
  }
}
