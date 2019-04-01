package com.mj.sbt.build

import com.typesafe.sbt.SbtGit.git
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile
import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoKeys.{buildInfoOptions, _}
import sbtbuildinfo.{BuildInfoKey, _}


object Defaults {

  def organization = "com.mj"

  def scalaVersion = "2.11.12"

  def version = "0.1-SNAPSHOT"

  def defaultDockerRepository = "192.168.0.1"

  def defaultSchemaRegistryUrl = s"http://$defaultDockerRepository:18081"

  def scalacOptions = Seq(
    "-unchecked",
    "-deprecation:false",
    "-feature",
    "-encoding",
    "utf8",
    "-Xfatal-warnings",
    "-target:jvm-1.8"
  )

  def javacOptions = Seq(
    "-source",
    "1.8",
    "-target",
    "1.8"
  )

  val settings = Seq(
    (Keys.organization in ThisBuild) := organization,
    (Keys.scalaVersion in ThisBuild) := scalaVersion,
    (Keys.version in ThisBuild) := version,
    (Keys.scalacOptions in Compile in ThisBuild) := scalacOptions,
    (Keys.javacOptions in Compile in ThisBuild) := javacOptions,
    (Keys.fork in Test in ThisBuild) := true,
//    (blockadeUris in ThisBuild) := Seq(file("blockade.json").toURI),
    (scalafmtOnCompile in ThisBuild) := true,
    (Keys.updateOptions in ThisBuild) := (Keys.updateOptions in ThisBuild).value.withCachedResolution(true),
    Keys.libraryDependencies += "com.github.ghik" %% "silencer-lib" % "0.5",
    Keys.libraryDependencies += compilerPlugin("com.github.ghik" %% "silencer-lib" % "0.5")
  )
}

object BuildInfoSettings {

  val settings = Seq(
    buildInfoKeys ++= Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      git.gitHeadCommit,
      git.gitCurrentBranch,
      "packageBase" -> s"com.mj.build.info.${name.value.replace('-', '.').replace("import", "metadata")}"
    ),
    buildInfoPackage := s"com.mj.build.info.${name.value.replace('-', '.').replace("import", "metadata")}",
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoOptions += BuildInfoOption.ToMap
  )
}

