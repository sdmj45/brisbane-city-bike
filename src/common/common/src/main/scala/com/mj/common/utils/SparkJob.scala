package com.mj.common.utils

import com.mj.common.settings.Settings._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait SparkJob {
  lazy val sparkConfig: SparkConf = new SparkConf()
    .setAppName(sparkSettings.appName)
    .setMaster(sparkSettings.master)

  implicit val spark = SparkSession.builder
    .config(sparkConfig)
    .getOrCreate()

}
