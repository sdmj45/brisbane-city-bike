package com.mj.common.utils

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType

object Transformer {
  private val LATITUDE_COL: String = "latitude"
  private val LONGITUDE_COL: String = "longitude"

  def apply(df: DataFrame)(implicit spark: SparkSession): DataFrame = {
    (transformeFromCoordinates andThen dropLatitudeOrLongitudeTypeNoNumber andThen dropLatitudeOrLongitudeNullable)(
      df)
  }

  private def transformeFromCoordinates: DataFrame => DataFrame =
    df =>
      df.withColumnRenamed("coordinates.latitude", LATITUDE_COL)
        .withColumnRenamed("coordinates.longitude", LONGITUDE_COL)

  private def dropLatitudeOrLongitudeNullable: DataFrame => DataFrame =
    df =>
      df.na
        .drop(Seq(LATITUDE_COL, LONGITUDE_COL))

  private def dropLatitudeOrLongitudeTypeNoNumber: DataFrame => DataFrame =
    df =>
      df.withColumn(LATITUDE_COL, trim(col(LATITUDE_COL)).cast(DoubleType))
        .withColumn(LONGITUDE_COL, trim(col(LONGITUDE_COL)).cast(DoubleType))

}
