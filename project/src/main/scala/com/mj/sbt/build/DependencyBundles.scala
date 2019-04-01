package com.mj.sbt.build

import sbt._

object Dependencies extends DependencyBundles {

  object Library {

    def settings = Seq(
      Keys.libraryDependencies ++=
        spark ++
          frameless ++
          typesafeConfig ++
          scalatest ++
          logback.map(_.withConfigurations(Some("provided"))),
      Keys.dependencyOverrides ++= jackson
    )
  }

  object Service {

    def settings = Seq(
      Keys.libraryDependencies ++=
        spark ++
          frameless ++
          joda ++
          json4s ++
          macwire ++
          typesafeConfig ++
          logging ++
          logback ++
          scalatest,
      Keys.dependencyOverrides ++= jackson
    )

  }

}

trait DependencyBundles {
  val spark = Seq(
    "org.apache.spark" %% "spark-core" % Versions.spark % "provided,test",
    "org.apache.spark" %% "spark-sql" % Versions.spark % "provided,test",
    "org.apache.spark" %% "spark-mllib" % Versions.spark % "provided,test"
  )

  val log4JExclusions = Seq(
    ExclusionRule(organization = "org.apache.logging.log4j", name = "log4j"),
    ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12"),
    ExclusionRule(organization = "org.slf4j", name = "log4j-over-slf4j"),
    ExclusionRule(organization = "log4j", name = "log4j")
  )

  val jackson = Seq(
    "com.fasterxml.jackson.core" % "jackson-databind" % Versions.jackson,
    "com.fasterxml.jackson.core" % "jackson-core" % Versions.jackson,
    "com.fasterxml.jackson.core" % "jackson-annotations" % Versions.jackson,
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % Versions.jackson
  )

  val joda = Seq(
    "joda-time" % "joda-time" % Versions.jodaTime,
    "org.joda" % "joda-convert" % Versions.jodaConvert
  )

  val macwire = Seq(
    "com.softwaremill.macwire" %% "macros" % Versions.macwire % "provided",
    "com.softwaremill.macwire" %% "util" % Versions.macwire,
    "com.softwaremill.macwire" %% "proxy" % Versions.macwire
  )

  val logging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging,
    "org.log4s" %% "log4s" % Versions.log4s,
    "org.slf4j" % "slf4j-api" % Versions.slf4j,
    "org.slf4j" % "jcl-over-slf4j" % Versions.slf4j,
    "org.slf4j" % "jul-to-slf4j" % Versions.slf4j
  )

  val jacksonExclusions = Seq(
    ExclusionRule(organization = "com.fasterxml.jackson.core"),
    ExclusionRule(organization = "org.codehaus.jackson")
  )

  val json4s = Seq(
    "org.json4s" %% "json4s-jackson" % Versions.json4s,
    "org.json4s" %% "json4s-ext" % Versions.json4s
  )

  lazy val typesafeConfig = Seq(
    "com.typesafe" % "config" % Versions.typesafeConfig,
    "com.github.kxbmap" %% "configs" % Versions.kxbmap,
    "com.github.pureconfig" %% "pureconfig" % Versions.pureConfig
  )

  val scalatest = Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatest % "it,test",
    "com.github.tomakehurst" % "wiremock" % Versions.wiremock % "test",
    "org.scalacheck" %% "scalacheck" % Versions.scalacheck % "test"
  ).map(_.excludeAll(log4JExclusions: _*))

  val logback = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logback
  ).map(_.excludeAll(log4JExclusions: _*))

  val frameless = Seq(
    "org.typelevel" %% "frameless-dataset" % Versions.frameless,
    "org.typelevel" %% "frameless-ml" % Versions.frameless,
    "org.typelevel" %% "frameless-cats" % Versions.frameless
  )

}
