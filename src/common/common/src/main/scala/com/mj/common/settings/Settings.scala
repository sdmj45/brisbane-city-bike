package com.mj.common.settings

import com.typesafe.config.ConfigFactory
import pureconfig.generic.auto._

import scala.util.Try

case class SparkSettings(master: String, appName: String)

case class AppSettings(inputPath: String, modelPath: String, outputPath: String)

object Settings {
  val config = ConfigFactory.load()

  lazy val sparkSettings = Try {
    val master: String = config.getString("spark.master")
    val appName: String = config.getString("spark.app-name")

    SparkSettings(master, appName)
  }.getOrElse(throw new Exception("not find conf for SparkSettings"))

  lazy val appSettings: AppSettings =
    pureconfig
      .loadConfig[AppSettings]("app")
      .fold(_ => throw new Exception("not find conf for AppSettings"), r => r)
}
